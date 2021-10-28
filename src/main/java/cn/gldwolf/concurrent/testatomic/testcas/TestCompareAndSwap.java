package cn.gldwolf.concurrent.testatomic.testcas;

import cn.gldwolf.concurrent.threadpool.ThreadPoolUtils;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 模拟 CAS(Compare-And-Swap) 算法<br/>
 * CAS 是硬件对并发的支持，针对多处理器操作而设计的一种特殊的 CPU 指令，用于管理对共享数据的并发访问。
 * CAS 是一种<strong>无锁的非阻塞</strong>算法的实现。
 * <br/>
 * CAS 包含了 3 个操作数：
 * <ul>
 *     <li>内存值 V (value) </li>
 *     <li>预期原值 A (expectValue) </li>
 *     <li>新值 B (newValue) </li>
 * </ul>
 *
 * @author gldwolf
 */
public class TestCompareAndSwap {
    public static void main(String[] args) {
        final CompareAndSwap cas = new CompareAndSwap();
        final ThreadPoolExecutor executors = ThreadPoolUtils.getExecutors(1000, "TestCompareAndSwap");
        for (int i = 0; i < 1000; i++) {
            executors.execute(() -> {
                 // 逻辑为：
                 // 在修改一个变量值时，先获取到该变量的原始值，保存起来，然后对值进行修改，在设置值的时候，看现在变量的值是否是读取时的值，如果是则设置
                int expectValue = cas.getValue();
                System.out.println(cas.compareAndSet(expectValue, (int) Math.random() * 101));
            });
        }
    }
}

class CompareAndSwap {
    public int value;

    /**
     * 获取内存值
     */
    public synchronized int getValue() {
        return value;
    }

    /**
     * 比较并交换，对外不可见
     */
    private synchronized int compareAndSwap(int expectValue, int newValue) {
        int oldValue = value;
        // 内存值和预估值一致就替换
        if (oldValue == expectValue) {
            this.value = newValue;
        }
        return oldValue;
    }

    /**
     * 设置新值：调用比较并交换，看期望值和原来的值是否一致
     */
    public synchronized boolean compareAndSet(int expectValue, int newValue) {
        return expectValue == compareAndSwap(expectValue, newValue);
    }
}
