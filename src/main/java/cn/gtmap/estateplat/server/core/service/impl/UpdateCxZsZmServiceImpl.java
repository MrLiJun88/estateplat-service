package cn.gtmap.estateplat.server.core.service.impl;/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2018/1/3
 * @description 
 */

import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.model.CorrelationData;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.core.service.UpdateCxZsZmService;
import cn.gtmap.estateplat.server.rabbitmq.service.RabbitmqSendMessageService;
import cn.gtmap.estateplat.service.config.QlztService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UpdateCxZsZmServiceImpl implements UpdateCxZsZmService {
    @Autowired
    private QlztService qlztService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;

    private static final String CONFIGURATION_PARAMETER_ISUPDATEZT = "isUpdateZt";
    @Autowired
    private RabbitmqSendMessageService rabbitmqSendMessageService;

    @Override
    public List<Map<String, Object>> getCqzhListByProid(String proid) {
        List<Map<String, Object>> mapList=null;
        String wiid="";
        if(StringUtils.isNotBlank(proid)){
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
            if (pfWorkFlowInstanceVo != null){
                wiid = pfWorkFlowInstanceVo.getWorkflowIntanceId();
            }
        }
        if(StringUtils.equals(AppConfig.getProperty("bdcdj.use.rabbitmq"),"true")){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("wiid",wiid);
            if(StringUtils.isNotBlank(AppConfig.getProperty("bdcdj.rabbitmq.bdc.getcqztbywiid.queue.send"))){
                CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
                correlationData.setMessage(jsonObject);
                correlationData.setRoutingKey(AppConfig.getProperty("bdcdj.rabbitmq.bdc.getcqztbywiid.queue.send"));
                rabbitmqSendMessageService.sendMsg(wiid,correlationData.getRoutingKey(),JSON.toJSONString(jsonObject),correlationData);
            }
        }else{
            mapList=qlztService.listCqzt(wiid);
        }
        return mapList;
    }

    @Override
    public void updateCxZsZmByCqzh(List<Map<String, Object>> mapList) {
        String isUpdate = AppConfig.getProperty(CONFIGURATION_PARAMETER_ISUPDATEZT);
        if (StringUtils.equals(isUpdate, "true") && CollectionUtils.isNotEmpty(mapList)&&StringUtils.isNotBlank(AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateCxZsZmByCqzh.queue"))) {
            for(Map<String, Object> map : mapList){
                if(StringUtils.equals(AppConfig.getProperty("bdcdj.use.rabbitmq"),"true")){
                    JSONObject jsonObject=new JSONObject(map);
                    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
                    correlationData.setMessage(jsonObject);
                    correlationData.setRoutingKey(AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateCxZsZmByCqzh.queue"));
                    rabbitmqSendMessageService.sendMsg("",correlationData.getRoutingKey(),JSON.toJSONString(jsonObject),correlationData);
                }else {
                    qlztService.updateZszmZtByCqzh(map.get("cqzh").toString(),map.get("bdclx").toString());
                }
            }
        }
    }

    @Override
    public void updateCxZsZmByProid(String proid) {
        String isUpdate = AppConfig.getProperty(CONFIGURATION_PARAMETER_ISUPDATEZT);
        if (StringUtils.equals(isUpdate, "true") && StringUtils.isNotBlank(proid)) {
            if(StringUtils.equals(AppConfig.getProperty("bdcdj.use.rabbitmq"),"true")&&StringUtils.isNotBlank(AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateCxZsZmByProid.queue"))){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("proid",proid);
                CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
                correlationData.setMessage(jsonObject);
                correlationData.setRoutingKey(AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateCxZsZmByProid.queue"));
                rabbitmqSendMessageService.sendMsg(proid,correlationData.getRoutingKey(),JSON.toJSONString(jsonObject),correlationData);
            }else {
                qlztService.updateZszmZtByProid(proid);
            }
        }
    }

    @Override
    public void updateZszmZtDeleteEnvent(String proid) {
        String isUpdate = AppConfig.getProperty(CONFIGURATION_PARAMETER_ISUPDATEZT);
        if (StringUtils.equals(isUpdate, "true") && StringUtils.isNotBlank(proid)) {
            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                if(StringUtils.equals(AppConfig.getProperty("bdcdj.use.rabbitmq"),"true")&&StringUtils.isNotBlank(AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateZszmZtByDeleteEnvent.queue"))){
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("bdcZsList",bdcZsList);
                    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
                    correlationData.setMessage(jsonObject);
                    correlationData.setRoutingKey(AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateZszmZtByDeleteEnvent.queue"));
                    rabbitmqSendMessageService.sendMsg(proid,correlationData.getRoutingKey(),JSON.toJSONString(jsonObject),correlationData);
                }else{
                    qlztService.updateZszmZtByDeleteEnvent(bdcZsList);
                }
            }
        }
    }
}
