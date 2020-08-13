package cn.gtmap.estateplat.server.core.service.impl;/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2018/1/18
 * @description 
 */

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.model.CorrelationData;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.UpdateCxBdcdyZtService;
import cn.gtmap.estateplat.server.rabbitmq.service.RabbitmqSendMessageService;
import cn.gtmap.estateplat.service.config.QlztService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UpdateCxBdcdyZtServiceImpl implements UpdateCxBdcdyZtService {
    @Autowired
    private QlztService qlztService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private RabbitmqSendMessageService rabbitmqSendMessageService;

    private String isUpdate= AppConfig.getProperty("isUpdateZt");
    private static final String PARAMETER_BDCDYHLIST = "bdcdyhList";

    @Override
    public String updateBdcdyZtByBdcdyh(List<String> bdcdyhList, String bdclx) {
        if(StringUtils.equals(AppConfig.getProperty("bdcdj.use.rabbitmq"),"true")){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("bdclx",bdclx);
            jsonObject.put("bdcdyhList",bdcdyhList);
            if(StringUtils.isNotBlank(AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateBdcdyZtByBdcdyh.queue"))){
                CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
                correlationData.setMessage(jsonObject);
                correlationData.setRoutingKey(AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateBdcdyZtByBdcdyh.queue"));
                rabbitmqSendMessageService.sendMsg("",correlationData.getRoutingKey(),JSON.toJSONString(jsonObject),correlationData);
            }
            return null;
        }else{
            return qlztService.updateBdcdyZtByBdcdyh(bdcdyhList,bdclx);
        }
    }

    @Override
    public Boolean updateBdcdyZtByProid(String proid) {
        Boolean result=false;
        if(StringUtils.equals(isUpdate, "true") && StringUtils.isNotBlank(proid)){
            Map<String, Object> map=getMapByProid(proid);
            result=updateBdcdyZtByMap(map);
        }
        return result;
    }

    @Override
    public Map<String, Object> getMapByProid(String proid) {
        Map<String, Object> map=new ExtendedProperties();
        List<String> bdcdyhList=new LinkedList<String>();
        String bdclx="";
        if(StringUtils.equals(isUpdate, "true") && StringUtils.isNotBlank(proid)){
            //hzj 找到不动产单元号
            BdcXm bdcXm=bdcXmService.getBdcXmByProid(proid);
            bdclx=bdcdyService.getBdclxByPorid(proid);
            List<BdcXm> bdcXmList=new ArrayList<BdcXm>();
            if(bdcXm!=null){
                if(StringUtils.isNotBlank(bdcXm.getWiid())){
                    bdcXmList=bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                }else{
                    bdcXmList.add(bdcXm);
                }
            }
            if(CollectionUtils.isNotEmpty(bdcXmList)){
                for (BdcXm bdcxm : bdcXmList){
                    if(null !=bdcxm && StringUtils.isNotBlank(bdcxm.getProid())){
                        String bdcdyh=bdcdyService.getBdcdyhByProid(bdcxm.getProid());
                        if(StringUtils.isNotBlank(bdcdyh) && !bdcdyhList.contains(bdcdyh)){
                            bdcdyhList.add(bdcdyh);
                        }
                    }
                }
            }
        }

        if(CollectionUtils.isNotEmpty(bdcdyhList)) {
            map.put(PARAMETER_BDCDYHLIST, bdcdyhList);
        }
        if(bdclx != null && !"".equals(bdclx)) {
            map.put("bdclx", bdclx);
        }
        return map;
    }

    @Override
    public Boolean updateBdcdyZtByMap(Map<String, Object> map) {
        Boolean result=false;
        if(map !=null && null !=map.get(PARAMETER_BDCDYHLIST)){
            List<String> bdcdyhList=(List<String>)map.get(PARAMETER_BDCDYHLIST);
            String bdclx=(String) map.get("bdclx");
            if(CollectionUtils.isNotEmpty(bdcdyhList)){
                updateBdcdyZtByBdcdyh(bdcdyhList,bdclx);
                result=true;
            }
        }
        return result;
    }
}
