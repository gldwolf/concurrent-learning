package cn.gldwolf.concurrent.threadpool.threadfactory;

import java.util.concurrent.ThreadFactory;

/**
 * 自定义线程工厂
 */
public class MyThreadFactory implements ThreadFactory {
    private final String threadNamePrefix;
    private int threadNumber = 0;

    public MyThreadFactory(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, this.threadNamePrefix + "-" + threadNumber++);
    }
}