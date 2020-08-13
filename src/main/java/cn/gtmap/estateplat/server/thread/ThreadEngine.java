package cn.gtmap.estateplat.server.thread;

import com.gtis.spring.Container;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 线程池容器处理
 *
 * @author lst
 * @version v1.0, 18-06-12
 * @description 控制线程池 将批量的逻辑分批次处理
 */
@Component
public class ThreadEngine {

    /**
     * log日志对象
     *
     * @author lst
     * @description log日志对象
     */
    private static final  Logger LOGGER = LoggerFactory.getLogger(ThreadEngine.class);

    /**
     * 定义最大支持的线程数
     */
    final Integer maxSize=com.gtis.config.AppConfig.getIntProperty("thread.max",150);

    /**
     *  多线程调用主方法
     * @param list
     * @param wait
     * @return
     */
    public <T>List<T> excuteThread(List<T> list, boolean wait) {
        if (CollectionUtils.isNotEmpty(list)) {
            //线程池管理实例
            ThreadPoolTaskExecutor taskExecutor= (ThreadPoolTaskExecutor) Container.getBean("bdcThreadTaskExecutor");
            int countSize = list.size();
            //验证当前所剩线程数 走线程分配规则
            threadRule(countSize,0,taskExecutor);
            for (int i = 0; i < countSize; i++) {
                taskExecutor.execute((Runnable) list.get(i));
            }
            //是否需要等待
            if(wait){
                shutDownThread(taskExecutor);
            }
        }
        return list;
    }

    /**
     * 等待线程池中所有线程结束后关闭
     */
    private void shutDownThread(ThreadPoolTaskExecutor taskExecutor) {
        //线程池
        ThreadPoolExecutor threadPoolExecutor = taskExecutor.getThreadPoolExecutor();
        //lst 队列任务
        BlockingQueue<Runnable> queue = threadPoolExecutor.getQueue();
        while (true) {
            int count = threadPoolExecutor.getActiveCount();
            int queueSize = queue.size();
            if (count == 0 && queueSize==0) {
                taskExecutor.destroy();
                break;
            } else {
                LOGGER.info("线程池尚在工作中，当前触发{}个线程，队列中存在{}个任务排队", count,queueSize);
                try {
                    Thread.sleep(500L);
                } catch (Exception e) {
                    LOGGER.error(null,e);
                }
            }
        }
    }

    /**
     * 线程分配规则
     * @param size
     * @param count
     */
    private  void threadRule(Integer size, int count, ThreadPoolTaskExecutor taskExecutor){
        //剩余线程数
        Integer surplusSize=maxSize-CommonThread.getSumThread();
        //是否还有剩余线程可用
        boolean hasThread=surplusSize > 0;
        //线程池maxSize
        Integer maxPoolSize=taskExecutor.getMaxPoolSize();
        //有的情况
        if(hasThread){
            //不足最大线程时 改变线程池最大线程数
            Integer setSize=maxPoolSize;
            if(size < maxPoolSize){
                if(size > surplusSize){
                    setSize=surplusSize;
                }else{
                    setSize=size;
                }
            }else{
                if(surplusSize < maxPoolSize){
                    setSize=surplusSize;
                }
            }
            taskExecutor.setCorePoolSize(setSize);
            taskExecutor.setMaxPoolSize(setSize);
        }else{
            //无空余线程时 所需线程数较少时直接走单线程 否则等待
            if(size < 10){
                taskExecutor.setCorePoolSize(1);
                taskExecutor.setMaxPoolSize(1);
            }else{
                //等待时间超过5s的话给予5个线程进行处理
                if(count>10){
                    taskExecutor.setCorePoolSize(5);
                    taskExecutor.setMaxPoolSize(5);
                }else{
                    try {
                        Thread.sleep(500L);
                        ++count;
                        threadRule(size,count,taskExecutor);
                    } catch (Exception e) {
                        LOGGER.error(null,e);
                    }
                }
            }
        }
    }
}
