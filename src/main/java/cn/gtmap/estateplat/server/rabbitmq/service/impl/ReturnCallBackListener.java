package cn.gtmap.estateplat.server.rabbitmq.service.impl;

import cn.gtmap.estateplat.server.core.service.BdcMessageLogService;
import cn.gtmap.estateplat.server.model.BdcMessageLog;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/7/19
 * @description 消息发送失败返回监听器
 */
@Service
public class ReturnCallBackListener implements RabbitTemplate.ReturnCallback {
    @Autowired
    private BdcMessageLogService bdcMessageLogService;

    private Logger logger = LoggerFactory.getLogger(ReturnCallBackListener.class);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     * @return
     * @description 当消息从交换机到队列失败时，该方法被调用。（若成功，则不调用）,需要注意的是：该方法调用后，MsgSendConfirmCallBack中的confirm方法也会被调用，且ack = true
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        try{
            logger.info(MessageFormat.format("return--message:ReturnCallback:{0},{1},{2},{3},{4},{5}", message, replyCode, replyText, exchange, routingKey));
            if(message.getMessageProperties().getCorrelationId() != null) {
                String correlationId = new String(message.getMessageProperties().getCorrelationId(),"UTF-8");
                BdcMessageLog bdcMessageLog = bdcMessageLogService.getBdcMessageLogById(correlationId);
                if(bdcMessageLog != null){
                    bdcMessageLog.setFszt(ParamsConstants.MESSAGE_FSZT_WFS);
                    bdcMessageLog.setGxsj(CalendarUtil.getCurHMSDate());
                    bdcMessageLogService.saveOrUpdateBdcMessageLog(bdcMessageLog);
                    logger.info("update message send status ==="+ JSON.toJSONString(bdcMessageLog));
                }
            }
        }catch (Exception e) {
            logger.error("message return callback exception："+e);
        }
    }
}
