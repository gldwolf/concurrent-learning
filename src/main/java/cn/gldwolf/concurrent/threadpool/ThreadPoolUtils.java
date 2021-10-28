package cn.gldwolf.concurrent.threadpool;

import cn.gldwolf.concurrent.threadpool.threadfactory.MyThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池的简单工具类，用于获取一个线程池，corePoolSize 和 maximumPoolSize 相同，都为 executorNumber 大小
 * @author gldwolf
 */
public class ThreadPoolUtils {
    public static ThreadPoolExecutor getExecutors(int executorNumber, String threadNamePrefix) {
        return new ThreadPoolExecutor(1000,
                1000, 50, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1000, true),
                new MyThreadFactory(threadNamePrefix));
    }
}
