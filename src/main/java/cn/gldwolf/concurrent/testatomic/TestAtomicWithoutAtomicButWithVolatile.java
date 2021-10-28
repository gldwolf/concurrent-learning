package cn.gldwolf.concurrent.testatomic;

import cn.gldwolf.concurrent.threadpool.ThreadPoolUtils;
import cn.gldwolf.concurrent.threadpool.threadfactory.MyThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 在 serialNumber 前面加了 volatile 关键字后，仍得不到期望最大值 999，可见 volatile 关键字<strong>并不能</strong>保证原子性操作！
 *
 * @author gldwolf
 */
public class TestAtomicWithoutAtomicButWithVolatile {
    public static void main(String[] args) {
        AtomicDemoWithoutAtomicButWithVolatile adwtbwv = new AtomicDemoWithoutAtomicButWithVolatile();
        final ThreadPoolExecutor executors = ThreadPoolUtils.getExecutors(1000, "TestAtomicWithoutAtomicButWithVolatile");
        for (int i = 0; i < 1000; i++) {
            executors.execute(adwtbwv);
        }
        executors.shutdown();
    }
}

class AtomicDemoWithoutAtomicButWithVolatile implements Runnable {
    private volatile int serialNumber = 0;

    public int getSerialNumber() {
        return serialNumber++;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": " + getSerialNumber());
    }
}