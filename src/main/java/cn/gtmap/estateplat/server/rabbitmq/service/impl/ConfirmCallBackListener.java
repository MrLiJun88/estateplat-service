package cn.gtmap.estateplat.server.rabbitmq.service.impl;

import cn.gtmap.estateplat.server.core.service.BdcMessageLogService;
import cn.gtmap.estateplat.server.model.BdcMessageLog;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.alibaba.fastjson.JSON;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/7/19
 * @description 消息确认监听器
 */
@Service
public class ConfirmCallBackListener implements RabbitTemplate.ConfirmCallback {
    @Resource(name = "bdcAmqpTemplate")
    private RabbitTemplate bdcAmqpTemplate;
    @Autowired
    private BdcMessageLogService bdcMessageLogService;

    private Logger logger = LoggerFactory.getLogger(ConfirmCallBackListener.class);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param correlationData
     * @param ack
     * @return
     * @description 当消息发送到交换机（exchange）时，该方法被调用。1.如果消息没有到exchange,则ack=false；2.如果消息到达exchange,则ack=true
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack) {
        try {
            if(ack) {
                logger.info("send message successful:correlationData({}),ack({})", correlationData, ack);
                cn.gtmap.estateplat.server.core.model.CorrelationData messageCorrelationData = null;
                if (correlationData instanceof cn.gtmap.estateplat.server.core.model.CorrelationData) {
                    messageCorrelationData = (cn.gtmap.estateplat.server.core.model.CorrelationData) correlationData;
                }
                BdcMessageLog bdcMessageLog = bdcMessageLogService.getBdcMessageLogById(correlationData.getId());
                if(bdcMessageLog != null){
                    bdcMessageLog.setFszt(ParamsConstants.MESSAGE_FSZT_YFS);
                    bdcMessageLog.setGxsj(CalendarUtil.getCurHMSDate());
                    bdcMessageLogService.saveOrUpdateBdcMessageLog(bdcMessageLog);
                    logger.info("update message send status ==="+ JSON.toJSONString(bdcMessageLog));
                }
            }else{
                if (correlationData instanceof cn.gtmap.estateplat.server.core.model.CorrelationData) {
                    cn.gtmap.estateplat.server.core.model.CorrelationData messageCorrelationData = (cn.gtmap.estateplat.server.core.model.CorrelationData) correlationData;
                    String exchange = messageCorrelationData.getExchange();
                    Object message = messageCorrelationData.getMessage();
                    String routingKey = messageCorrelationData.getRoutingKey();
                    int retryCount = messageCorrelationData.getRetryCount();
                    logger.info("send message failed:correlationData({}),ack({}),重试次数：retryCount({})", correlationData, ack, retryCount);

                    //消息重发
                    int defaultRetryCount = 0;
                    String defaultRetryCountStr = AppConfig.getProperty("message.default.retrycount");
                    if(StringUtils.isNotBlank(defaultRetryCountStr)){
                        defaultRetryCount = Integer.parseInt(defaultRetryCountStr);
                    }else{
                        defaultRetryCount = ParamsConstants.MESSAGE_DEFAULT_RETRYCOUNT;
                    }
                    if(retryCount < defaultRetryCount) {
                        //重试次数+1
                        messageCorrelationData.setRetryCount(retryCount + 1);
                        bdcAmqpTemplate.convertAndSend(exchange,routingKey,message,messageCorrelationData);
                    }
                }
            }
        }catch (Exception e) {
            logger.error("message confirm exception ：" + e);
        }
    }
}
