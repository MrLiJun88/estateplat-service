package cn.gtmap.estateplat.server.rabbitmq.service.impl;
/*
 * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
 * @version 1.0, 2019-02-22
 * @description 
 */

import cn.gtmap.estateplat.model.exchange.national.QlfQlZxdj;
import cn.gtmap.estateplat.server.core.service.BdcMessageLogService;
import cn.gtmap.estateplat.server.core.service.SyncQlztService;
import cn.gtmap.estateplat.server.model.BdcMessageLog;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.service.exchange.QzLjzService;
import cn.gtmap.estateplat.service.exchange.share.RealEstateShareService;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gtis.config.AppConfig;
import com.rabbitmq.client.Channel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RabbitMqConsumerListener implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(RabbitMqConsumerListener.class);
    @Autowired
    private SyncQlztService syncQlztService;
    @Autowired
    private RealEstateShareService realEstateShareService;
    @Autowired
    private BdcMessageLogService bdcMessageLogService;

    @Override
    public void onMessage(Message message, Channel channel) {
        String key=message.getMessageProperties().getReceivedRoutingKey();
        try {
            byte[] body = message.getBody();
            String s= new String(body, "UTF-8");
            //将jsonArray字符串转化为JSONArray
            JSONObject jsonObject = JSONObject.parseObject(s);

            if(StringUtils.equals(key, AppConfig.getProperty("bdcdj.rabbitmq.bdc.getgxyproidsbywiidforback.queue.return"))&&jsonObject!=null&&jsonObject.containsKey("proidList") && null != jsonObject.get("proidList")){
                System.out.println("***"+AppConfig.getProperty("bdcdj.rabbitmq.bdc.getgxyproidsbywiidforback.queue.return"));
                List<String> proidList = null;
                List<String> yproidList = new LinkedList<String>();
                if (null != jsonObject && jsonObject.containsKey("proidList") && null != jsonObject.get("proidList")) {
                    proidList = (List<String>) jsonObject.get("proidList");
                    if (null != jsonObject.get("yproidList")) {
                        yproidList = (List<String>) jsonObject.get("yproidList");
                    }
                    realEstateShareService.shareReturnProjectData2Db(proidList, yproidList);
                }
            }
            if(StringUtils.equals(key, AppConfig.getProperty("bdcdj.rabbitmq.bdc.getgxyproidsbywiidfordelet.queue.return"))&&jsonObject!=null&&jsonObject.containsKey("proidList") && null != jsonObject.get("proidList")){
                System.out.println("***"+AppConfig.getProperty("bdcdj.rabbitmq.bdc.getgxyproidsbywiidfordelet.queue.return"));
                Map map=new HashedMap();
                map.put("proidList",jsonObject.get("proidList"));
                map.put("yproidList",jsonObject.get("yproidList"));
                syncQlztService.updateGxztAfterDelet(map);
            }
            if(StringUtils.equals(key, AppConfig.getProperty("bdcdj.rabbitmq.bdc.getcqztbywiid.queue.return"))&&jsonObject!=null&&jsonObject.containsKey("cqztList")){
                System.out.println("***"+AppConfig.getProperty("bdcdj.rabbitmq.bdc.getcqztbywiid.queue.return"));
                syncQlztService.updateQlztAfterDelet((List<Map<String, Object>>) jsonObject.get("cqztList"));

            }
            if(StringUtils.equals(key, AppConfig.getProperty("bdcdj.rabbitmq.bdc.getgxlistbywiid.queue.return"))&&jsonObject!=null&&jsonObject.containsKey("qlfQlZxdjList")){
                //logger.error(CommonUtil.formatEmptyValue(jsonObject.getJSONArray("qlfQlZxdjList")));
                List<QlfQlZxdj> qlfQlZxdjList = new ArrayList<QlfQlZxdj>();
                JSONArray jsonArray = jsonObject.getJSONArray("qlfQlZxdjList");
                if(jsonArray != null && !jsonArray.isEmpty()){
                    for (Object object : jsonArray){
                        JSONObject qllfJsonObject = (JSONObject)object;
                        if(qllfJsonObject != null){
                            QlfQlZxdj qlfQlZxdj = new QlfQlZxdj();
                            Long timestamp = null;
                            Long zxTimestamp = null;
                            if(qllfJsonObject.get("djsj") instanceof  JSONObject){
                                JSONObject dateJsonObject = qllfJsonObject.getJSONObject("djsj");
                                if(dateJsonObject != null){
                                    timestamp = dateJsonObject.getLong("time");
                                    qllfJsonObject.put("djsj", "");
                                }
                            }
                            if(qllfJsonObject.get("zxsj") instanceof  JSONObject){
                                JSONObject dateJsonObject = qllfJsonObject.getJSONObject("zxsj");
                                if(dateJsonObject != null){
                                    zxTimestamp = dateJsonObject.getLong("time");
                                    qllfJsonObject.put("zxsj", "");
                                }
                            }
                            qlfQlZxdj = JSON.parseObject(CommonUtil.formatEmptyValue(qllfJsonObject),QlfQlZxdj.class);
                            if(timestamp != null){
                                qlfQlZxdj.setDjsj(new Date(timestamp));
                            }
                            if(zxTimestamp != null){
                                qlfQlZxdj.setZxsj(new Date(zxTimestamp));
                            }
                            qlfQlZxdjList.add(qlfQlZxdj);
                        }
                    }
                }
                //qlfQlZxdjList = JSON.parseArray(CommonUtil.formatEmptyValue(jsonObject.get("qlfQlZxdjList")),QlfQlZxdj.class);
                realEstateShareService.shareCancellationData2Db(qlfQlZxdjList);
            }

            logger.info("consumer--:" + message.getMessageProperties() + ":" + new String(message.getBody()));
            if(message.getMessageProperties().getCorrelationId() != null) {
                String correlationId = new String(message.getMessageProperties().getCorrelationId(),"UTF-8");
                BdcMessageLog bdcMessageLog = bdcMessageLogService.getBdcMessageLogById(correlationId);
                if(bdcMessageLog != null){
                    bdcMessageLog.setXfzt(ParamsConstants.MESSAGE_XFZT_YXF);
                    bdcMessageLog.setGxsj(CalendarUtil.getCurHMSDate());
                    bdcMessageLogService.saveOrUpdateBdcMessageLog(bdcMessageLog);
                    logger.info("update message send status ==="+ JSON.toJSONString(bdcMessageLog));
                }
            }
            basicACK(message, channel);
        } catch (Exception e) {
            basicNACK(message, channel); //处理异常，mq重回队列
            logger.error("context", e);
        }
    }

    //正常消费掉后通知mq服务器移除此条mq
    private void basicACK(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            logger.error("通知服务器移除mq时异常，异常信息：" + e);
        }
    }

    //处理异常，mq重回队列
    private void basicNACK(Message message, Channel channel) {
        try {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        } catch (IOException e) {
            logger.error("mq重新进入服务器时出现异常，异常信息：" + e);
        }
    }
}
