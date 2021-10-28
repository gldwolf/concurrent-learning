package cn.gldwolf.concurrent.testatomic;

import cn.gldwolf.concurrent.threadpool.ThreadPoolUtils;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <strong>原子变量：</strong>在 java.util.concurrent.atomic 包下提供了一些原子变量。
 * CAS(Compare-And-Swap) 算法保证数据变量的原子性。
 * <ol>
 *     <li>CAS 算法是硬件对并发操作的支持</li>
 *     <li>
 *         CAS 包含了三个操作数：
 *         <ul>
 *             <li>内存地址 V</li>
 *             <li>预期原值 A</li>
 *             <li>新值 B</li>
 *             如果内存地址中的值与预期原值相匹配，那么处理器就会自动将该地址中的值更新为新值，否则，处理器不做任何操作。
 *             无论哪种情况，都会在 CAS 指令之前返回该位置的值。（在 CAS 的一些特殊情况下仅返回 CAS 是否成功，而不是取出当前值）
 *         </ul>
 *     </li>
 * </ol>
 * 通过执行可以看出，AtomicInteger 的 serialNumber 具有原子性，能够保证得到期望的最大值 999，是线程安全的，论证：@see {@link }
 * @author gldwolf
 */
public class TestAtomicWithAtomic {
    public static void main(String[] args) {
        final AtomicDemoWithAtomic adwa = new AtomicDemoWithAtomic();
        final ThreadPoolExecutor executors = ThreadPoolUtils.getExecutors(1000, "TestAtomicWithAtomic");
        for (int i = 0; i < 1000; i++) {
            executors.execute(adwa);
        }
        executors.shutdown();
    }
}

class AtomicDemoWithAtomic implements Runnable {
    private final AtomicInteger serialNumber = new AtomicInteger(0);

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": " + getSerialNumber());
    }

    public int getSerialNumber() {
        return serialNumber.getAndIncrement();
    }
}