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
 *     <li>内存地址值 V (value) </li>
 *     <li>预期原值 A (expectValue) </li>
 *     <li>新值 B (newValue) </li>
 *     如果内存地址中的值与预期原值相匹配，那么处理器就会自动将该地址中的值更新为新值，否则，处理器不做任何操作。
 *     无论哪种情况，都会在 CAS 指令之前返回该位置的值。（在 CAS 的一些特殊情况下仅返回 CAS 是否成功，而不是取出当前值）
 *     通常将 CAS 用于同步的方式是从地址 V 读取值 A，执行计算来获取新值 B，然后使用 CAS 指令将 V 中的值从 A 更新为 B，
 *     如果 V 处的值与预期原值 A 相同，则 CAS 操作成功。
 *     类似于 CAS 的指令允许算法执行读-修改-写操作，而无需害怕其它线程同时修改变量，因为如果其它线程修改变量，
 *     那么 CAS 会检测到（通过对比 V 中的值与预期原值）并失败，算法可以对该操作重新计算。
 * </ul>
 * <strong>CAS 的目的：</strong>
 * 利用 CPU 的 CAS 指令，同时借助 JNI 来完成 JAVA 的非阻阻塞算法。其它原子操作都是利用类似的特性完成的。
 * 而整个 JUC 都是建立在 CAS 基础上的，因此对于 synchronized 阻塞算法，JUC 在性能上有了很大的提升。
 * <br/><br/>
 * <strong>例如 AtomicInteger 的实现是这样的：</strong>
 * 读取内存中的值
 * <br/>
 * <br/>
 * <strong>CAS 存在的问题：</strong>
 * CAS 虽然高效地解决原子性操作，但是 CAS 仍然存在三大问题：
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
    public volatile int value;

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
