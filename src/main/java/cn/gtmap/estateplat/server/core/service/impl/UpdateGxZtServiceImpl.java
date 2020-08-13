package cn.gtmap.estateplat.server.core.service.impl;/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2018/1/5
 * @description 
 */

import cn.gtmap.estateplat.model.exchange.national.QlfQlZxdj;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.model.CorrelationData;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.UpdateGxZtService;
import cn.gtmap.estateplat.server.rabbitmq.service.RabbitmqSendMessageService;
import cn.gtmap.estateplat.service.config.GxztService;
import cn.gtmap.estateplat.service.exchange.share.RealEstateShareService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UpdateGxZtServiceImpl implements UpdateGxZtService {
    @Autowired
    private GxztService gxztService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private RealEstateShareService realEstateShareService;
    @Autowired
    private BdcXmService bdcXmService;

    private static final String PARAMETER_PROIDLIST = "proidList";
    private static final String PARAMETER_YPROIDLIST = "yproidList";
    @Autowired
    private RabbitmqSendMessageService rabbitmqSendMessageService;

    @Override
    public Map getGxZtByProid(String proid, String isCompara,String reason) {
        Map map = null;
        if (StringUtils.isNotBlank(proid)) {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
            String wiid = "";
            if (pfWorkFlowInstanceVo != null) {
                wiid = pfWorkFlowInstanceVo.getWorkflowIntanceId();
            }
            if(StringUtils.equals(AppConfig.getProperty("bdcdj.use.rabbitmq"),"true")){
                //hzj 删除的在此处没有调用过滤yproidList方法
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("wiid",wiid);
                jsonObject.put("isCompara",isCompara);
                if(StringUtils.equals(reason,"back")&&StringUtils.isNotBlank(AppConfig.getProperty("bdcdj.rabbitmq.bdc.getgxyproidsbywiidforback.queue.send"))){
                    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
                    correlationData.setMessage(jsonObject);
                    correlationData.setRoutingKey(AppConfig.getProperty("bdcdj.rabbitmq.bdc.getgxyproidsbywiidforback.queue.send"));
                    rabbitmqSendMessageService.sendMsg(wiid,correlationData.getRoutingKey(),JSON.toJSONString(jsonObject),correlationData);
                }else if(StringUtils.equals(reason,"delet")&&StringUtils.isNotBlank(AppConfig.getProperty("bdcdj.rabbitmq.bdc.getgxyproidsbywiidfordelet.queue.send"))){
                    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
                    correlationData.setMessage(jsonObject);
                    correlationData.setRoutingKey(AppConfig.getProperty("bdcdj.rabbitmq.bdc.getgxyproidsbywiidfordelet.queue.send"));
                    rabbitmqSendMessageService.sendMsg(wiid,correlationData.getRoutingKey(),JSON.toJSONString(jsonObject),correlationData);
                }
            }else {
                map = gxztService.getGxYproidsByWiid(wiid,isCompara);
            }

        }
        return map;
    }

    @Override
    public void updateGxztForBack(String proid) {
        if (StringUtils.isNotBlank(proid)) {
            if(StringUtils.equals(AppConfig.getProperty("bdcdj.use.rabbitmq"),"true")){
                getGxZtByProid(proid,"true","back");
            }else {
                Map map = getGxZtByProid(proid,"true",null);
                List<String> proidList = new LinkedList<String>();
                List<String> yproidList = new LinkedList<String>();
                if (null != map && map.containsKey(PARAMETER_PROIDLIST) && null != map.get(PARAMETER_PROIDLIST)) {
                    proidList = (List<String>) map.get(PARAMETER_PROIDLIST);
                    if (null != map.get(PARAMETER_YPROIDLIST)) {
                        yproidList = (List<String>) map.get(PARAMETER_YPROIDLIST);
                    }
                }
                realEstateShareService.shareReturnProjectData2Db(proidList, yproidList);
            }
        }
    }

    @Override
    public void updateGxztByProid(String proid) {
        if (StringUtils.isNotBlank(proid)) {
            List<QlfQlZxdj> qlfQlZxdjList = getGxListByProid(proid);
            if(CollectionUtils.isNotEmpty(qlfQlZxdjList)){
                realEstateShareService.shareCancellationData2Db(qlfQlZxdjList);
            }
        }
    }

    @Override
    public void updateGxztByMap(Map map) {
        List<String> proidList = new LinkedList<String>();
        List<String> yproidList = new LinkedList<String>();
        if (null != map && map.containsKey(PARAMETER_PROIDLIST) && null != map.get(PARAMETER_PROIDLIST)) {
            proidList = (List<String>) map.get(PARAMETER_PROIDLIST);
            if (null != map.get(PARAMETER_YPROIDLIST)) {
                yproidList = (List<String>) map.get(PARAMETER_YPROIDLIST);
            }
        }
        realEstateShareService.shareDeleteProjectData2Db(proidList, yproidList);
    }

    @Override
    public List<QlfQlZxdj> getGxListByProid(String proid) {
        List<QlfQlZxdj> qlfQlZxdjList = new LinkedList<QlfQlZxdj>();
        if (StringUtils.isNotBlank(proid)) {
            String wiid = "";
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if(bdcXm != null){
                wiid = bdcXm.getWiid();
            }
            if(StringUtils.equals(AppConfig.getProperty("bdcdj.use.rabbitmq"),"true")&&StringUtils.isNotBlank(AppConfig.getProperty("bdcdj.rabbitmq.bdc.getgxlistbywiid.queue.send"))){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("wiid",wiid);
                CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
                correlationData.setMessage(jsonObject);
                correlationData.setRoutingKey(AppConfig.getProperty("bdcdj.rabbitmq.bdc.getgxlistbywiid.queue.send"));
                rabbitmqSendMessageService.sendMsg(wiid,correlationData.getRoutingKey(),JSON.toJSONString(jsonObject),correlationData);
            }else {
                qlfQlZxdjList = gxztService.getGxListByWiid(wiid);
            }
        }
        return qlfQlZxdjList;
    }
}
