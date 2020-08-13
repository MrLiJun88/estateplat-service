package cn.gtmap.estateplat.server.quartz;

import cn.gtmap.estateplat.server.core.service.BdcMessageLogService;
import cn.gtmap.estateplat.server.model.BdcMessageLog;
import cn.gtmap.estateplat.server.rabbitmq.service.RabbitmqSendMessageService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/8/6
 * @description 消息定时器
 */
@Component
public class MessageQuartz {

    @Autowired
    private BdcMessageLogService bdcMessageLogService;
    @Autowired
    private RabbitmqSendMessageService rabbitmqSendMessageService;

    /**
     * log日志对象
     * @author hqz
     * @description log日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(MessageQuartz.class);

    /**
     * 判断上次定时任务是够结束的标识状态
     * @author hqz
     * @description 判断上次定时任务是够结束的标识状态(true:已结束,false:未结束)，未结束不会开启第二次任务
     */
    private boolean isFinish = true;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 消息重发定时器执行方法 在app-quartz.xml中配置定时器执行时间和间隔
     */
    public void execute()  {
         boolean retryQuartEnable = Boolean.parseBoolean(AppConfig.getProperty("message.retry.quart.enable"));
        if (retryQuartEnable) {
            if (isFinish) {
                try {
                    isFinish = false;
                    List<BdcMessageLog> bdcMessageLogList = bdcMessageLogService.getBdcMessageLogList(ParamsConstants.MESSAGE_FSZT_WFS);
                    rabbitmqSendMessageService.retryMsg(bdcMessageLogList);
                } catch(Exception e){
                    logger.error(e.getMessage());
                }finally {
                    isFinish = true;
                }
            }
        }
    }
}
