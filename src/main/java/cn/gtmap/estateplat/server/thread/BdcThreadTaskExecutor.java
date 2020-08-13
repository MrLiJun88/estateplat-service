package cn.gtmap.estateplat.server.thread;

import com.gtis.config.AppConfig;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author <a href="mailto:lisongtao@gtmap.cn">lisongtao</a>
 * @version 1.0  2018/6/13.
 * @description
 */
public class BdcThreadTaskExecutor extends ThreadPoolTaskExecutor {

    /**
     * 处理线程参数  构造函数
     *@author <a href="mailto:lisongtao@gtmap.cn">lisongtao</a>
     *@param
     *@return
     *@description
     */
    public BdcThreadTaskExecutor(){
        Integer maxSize = AppConfig.getIntProperty("thread.max",50);
        //lst 默认50  优先走thread.max配置 最大不能超过50或小于0
        if(maxSize > 50 || maxSize < 1){
            maxSize=50;
        }
        super.setCorePoolSize(maxSize);
        super.setMaxPoolSize(maxSize);
        super.setWaitForTasksToCompleteOnShutdown(true);
    }
}
