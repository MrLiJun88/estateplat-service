package cn.gtmap.estateplat.server.rabbitmq.service.impl;
/*
 * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
 * @version 1.0, 2019-02-22
 * @description rabbitmq发送消息服务
 */

import cn.gtmap.estateplat.server.core.model.CorrelationData;
import cn.gtmap.estateplat.server.core.service.BdcMessageLogService;
import cn.gtmap.estateplat.server.model.BdcMessageLog;
import cn.gtmap.estateplat.server.rabbitmq.service.RabbitmqSendMessageService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.alibaba.fastjson.JSON;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RabbitmqSendMessageServiceImpl implements RabbitmqSendMessageService {

    @Resource(name = "bdcAmqpTemplate")
    private RabbitTemplate bdcAmqpTemplate;
    @Autowired
    private BdcMessageLogService bdcMessageLogService;

    protected final Logger logger = LoggerFactory.getLogger(RabbitmqSendMessageServiceImpl.class);

    @Override
    @Transactional
    public void sendMsg(String proid, String routingKey, String messageJson, CorrelationData correlationData) {
        if(StringUtils.isNotBlank(messageJson)) {
            Date curHMSDate = CalendarUtil.getCurHMSDate();
            BdcMessageLog bdcMessageLog = new BdcMessageLog();
            bdcMessageLog.setId(correlationData.getId());
            bdcMessageLog.setProid(proid);
            bdcMessageLog.setCjsj(curHMSDate);
            bdcMessageLog.setXxdlm(routingKey);
            bdcMessageLog.setXxt(messageJson);
            bdcMessageLog.setFszt(ParamsConstants.MESSAGE_FSZT_WFS);
            bdcMessageLog.setXfzt(ParamsConstants.MESSAGE_XFZT_WXF);
            bdcMessageLog.setGxsj(curHMSDate);
            bdcMessageLogService.saveOrUpdateBdcMessageLog(bdcMessageLog);
            logger.info("insert message send status ==="+ JSON.toJSONString(bdcMessageLog));

            Message message = MessageBuilder.withBody(messageJson.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    //.setDeliveryMode(MessageDeliveryMode.PERSISTENT) //消息默认是持久化的
                    .setCorrelationId(correlationData.getId().getBytes()).build();
            bdcAmqpTemplate.convertAndSend(routingKey,message,correlationData);
        }
    }

    @Override
    public void retryMsg(List<BdcMessageLog> bdcMessageLogList) {
        if(CollectionUtils.isNotEmpty(bdcMessageLogList)) {
            for(BdcMessageLog bdcMessageLog:bdcMessageLogList) {
                CorrelationData correlationData = new CorrelationData(bdcMessageLog.getId());
                correlationData.setMessage(bdcMessageLog.getXxt());
                correlationData.setRoutingKey(bdcMessageLog.getXxdlm());
                Message message = MessageBuilder.withBody(bdcMessageLog.getXxt().getBytes())
                        .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                        //.setDeliveryMode(MessageDeliveryMode.PERSISTENT) //消息默认是持久化的
                        .setCorrelationId(correlationData.getId().getBytes()).build();
                bdcAmqpTemplate.convertAndSend(correlationData.getRoutingKey(),message,correlationData);
            }
        }
    }

}
