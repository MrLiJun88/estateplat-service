package cn.gtmap.estateplat.server.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:lisongtao@gtmap.cn">lisongtao</a>
 * @version 1.0  2018/8/7.
 * @description
 */
public abstract class CommonThread implements Runnable {

    /**
     * 线程数量
     */
    private  static AtomicInteger sumThread = new AtomicInteger(0);

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonThread.class);

    public  static Integer getSumThread() {
        return sumThread.get();
    }


    @Override
    public void run() {
        //自增  增加当前线程总数
        sumThread.incrementAndGet();
        //默认处理方法
        try {
            execute();
        } catch (Exception e) {
            LOGGER.error(null,e);
        }finally {
            //自减 减少当前线程总数
            sumThread.decrementAndGet();
        }
    }

    /**
     * 默认执行方法
     */
    public abstract void execute() throws Exception;

}
