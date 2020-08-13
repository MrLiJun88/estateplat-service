package cn.gtmap.estateplat.server.rabbitmq.service;


import cn.gtmap.estateplat.server.core.model.CorrelationData;
import cn.gtmap.estateplat.server.model.BdcMessageLog;

import java.util.List;

/*
 * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
 * @version 1.0, 2019-02-22
 * @description  rabbitmq发送消息服务
 */
public interface RabbitmqSendMessageService {

    /**
     * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     * @param proid
     * @param routingKey
     * @param message
     * @param correlationData
     * @return
     * @description 发送消息
     */
    void sendMsg(String proid, String routingKey, String message, CorrelationData correlationData);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcMessageLogList
     * @return
     * @description 重复消息
     */
    void retryMsg(List<BdcMessageLog> bdcMessageLogList);

}
