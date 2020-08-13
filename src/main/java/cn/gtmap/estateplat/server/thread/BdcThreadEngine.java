
package cn.gtmap.estateplat.server.thread;

import com.gtis.config.AppConfig;
import com.gtis.spring.Container;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 线程池容器处理
 *
 * @author lst
 * @version v1.0, 18-06-12
 * @description 控制线程池 将批量的逻辑分批次处理
 */
@Component
public class BdcThreadEngine {

    /**
     * log日志对象
     *
     * @author lst
     * @description log日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(BdcThreadEngine.class);

    /**
     * 线程池管理实例
     */
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    //定义信号量，限制线程池数量
    final Semaphore semaphore = new Semaphore(AppConfig.getIntProperty("threadPool.max",50));

    public <T>List<T> excuteThread(List<T> list) {
        boolean hasPool=semaphore.tryAcquire();
        if (CollectionUtils.isNotEmpty(list)) {
            taskExecutor= (ThreadPoolTaskExecutor)Container.getBean("bdcThreadTaskExecutor");
            if(!hasPool){
                taskExecutor.setCorePoolSize(1);
                taskExecutor.setMaxPoolSize(1);
            }
            double countSize = list.size();
            for (int i = 0; i < countSize; i++) {
                taskExecutor.execute((Runnable) list.get(i));
            }
            shutDownThread();
        }
        return list;
    }

    private void shutDownThread() {
        //线程池
        ThreadPoolExecutor threadPoolExecutor = taskExecutor.getThreadPoolExecutor();
        //lst 队列任务
        BlockingQueue<Runnable> queue = threadPoolExecutor.getQueue();
        while (true) {
            int count = taskExecutor.getActiveCount();
            int queueSize = queue.size();
            if (count == 0 && queueSize==0) {
                semaphore.release();
                taskExecutor.destroy();
                break;
            } else {
                logger.info("线程池尚在工作中，当前触发{}个线程，队列中存在{}个任务排队", count,queueSize);
                try {
                    Thread.sleep(500L);
                } catch (Exception e) {
                    logger.error(null,e);
                }
            }
        }
    }
}

