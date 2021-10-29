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
 *             通常将 CAS 用于同步的方式是从地址 V 读取值 A，执行计算来获取新值 B，然后使用 CAS 指令将 V 中的值从 A 更新为 B，
 *             如果 V 处的值与预期原值 A 相同，则 CAS 操作成功。
 *             类似于 CAS 的指令允许算法执行读-修改-写操作，而无需害怕其它线程同时修改变量，因为如果其它线程修改变量，
 *             那么 CAS 会检测到（通过对比 V 中的值与预期原值）并失败，算法可以对该操作重新计算。
 *         </ul>
 *     </li>
 * </ol>
 * 通过执行可以看出，AtomicInteger 的 serialNumber 具有原子性，能够保证得到期望的最大值 999，是线程安全的，论证：@see {@link cn.gldwolf.concurrent.testatomic.testcas.TestCompareAndSwap TestCompareAndSwap}
 * <br/>
 * <strong>看源码可以得出 AtomicInteger 的实现这样的：</strong>
 * <ol>
 *     <li>读取内存地址 V (offset) 中的值，得到预期原值</li>
 *     <li>对这个预期原值进行加减操作，得到新值 B</li>
 *     <li>拿预期原值与内存地址 V 中的现值(可以已经被其它线程修改，也可能没有修改)进行比较</li>
 *     <ul>
 *         <li>如果预期原值与内存中现值相同，则将新值写入到内存中</li>
 *         <li><strong>如果预期原值与内存中现值不同，则跳到 1 进行重复操作，所以预期原值会更新，
 *         而得到的新值也会更新，直到预期原值与内存中的现值相同，则更新成功</strong></li>
 *         <strong>由于 CAS 指令是一条指令，不可拆分，所以保证了这个操作是原子性的</strong>
 *     </ul>
 * </ol>
 * <strong>一个简单的说明失败的情况如何进行更新操作：</strong>
 * 假设在第一次读取到的期望原值为 1，进行 +1 操作，得到新值 2，此时进行 CAS 操作，执行 lock cmpxchg 指令(lock 期间的写操作会回写已修改的数据到主内存，同时通过缓存一致性协议让其它 CPU 相关缓存行失效)，
 * 此时其它 CPU core 应该是不能再操作此块内存，得到内存中的现值为 3，与期望原值不符，所以此次更新操作失败，
 * 则再重新读取期望原值假使为 4(因为可能其它线程又更新了内存中的现值)，对 4 进行 +1 操作，得到新值为 5，
 * 此时再次进行 CAS 操作，读取内存中的现值仍为 4，与期望原值相同，则将内存中的现值更新为 5。此时本次更新操作完成(即对目标变量进行了正确的 +1 操作，保证了原子性)！
 *
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