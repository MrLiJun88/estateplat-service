package cn.gtmap.estateplat.server.service.config.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.model.CorrelationData;
import cn.gtmap.estateplat.server.rabbitmq.service.RabbitmqSendMessageService;
import cn.gtmap.estateplat.server.service.config.ConfigRedundantFieldService;
import cn.gtmap.estateplat.service.config.RedundantFieldService;
import cn.gtmap.estateplat.service.config.RedundantMulFieldService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/9/18
 * @description
 */
@Service
public class ConfigRedundantFieldServiceImpl implements ConfigRedundantFieldService {
    @Autowired
    private RabbitmqSendMessageService rabbitmqSendMessageService;
    @Autowired
    private RedundantFieldService redundantFieldService;
    @Autowired
    private RedundantMulFieldService redundantMulFieldService;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async
    @Override
    public void synchronizationField(BdcXm bdcXm) {
        if (bdcXm != null) {
            try {
                if (StringUtils.equals(AppConfig.getProperty("bdcdj.use.rabbitmq"), "true")) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("proid", bdcXm.getProid());
                    if (StringUtils.isBlank(bdcXm.getBdcdyid()) && StringUtils.isNotBlank(AppConfig.getProperty("bdcdj.rabbitmq.bdc.synchronizationgdfield.queue"))) {
                        //不匹配不动产单元同步冗余字段
                        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
                        correlationData.setMessage(jsonObject);
                        correlationData.setRoutingKey(AppConfig.getProperty("bdcdj.rabbitmq.bdc.synchronizationgdfield.queue"));
                        rabbitmqSendMessageService.sendMsg(bdcXm.getProid(), correlationData.getRoutingKey(), JSON.toJSONString(jsonObject), correlationData);
                    } else if (StringUtils.isNotBlank(AppConfig.getProperty("bdcdj.rabbitmq.bdc.synchronizationbdcfield.queue"))) {
                        //匹配不动产单元同步冗余字段
                        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
                        correlationData.setMessage(jsonObject);
                        correlationData.setRoutingKey(AppConfig.getProperty("bdcdj.rabbitmq.bdc.synchronizationbdcfield.queue"));
                        rabbitmqSendMessageService.sendMsg(bdcXm.getProid(), correlationData.getRoutingKey(), JSON.toJSONString(jsonObject), correlationData);
                    }
                } else {
                    if (StringUtils.isBlank(bdcXm.getBdcdyid())) {
                        //不匹配不动产单元同步冗余字段
                        redundantFieldService.synchronizationGdField(bdcXm.getProid());
                    } else {
                        //匹配不动产单元同步冗余字段
                        redundantFieldService.synchronizationBdcField(bdcXm.getProid());
                    }
                }
            } catch (Exception e) {
                logger.error("ConfigRedundantFieldServiceImpl.synchronizationField", e);
            }
        }
    }

    /**
     * @param wiid 工作流实例ID
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 批量同步字段信息
     */
    @Override
    public void synchronizationField(String wiid) {
        redundantMulFieldService.synchronizationBdcField(wiid);
    }
}
