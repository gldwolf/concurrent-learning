package cn.gldwolf.concurrent.testatomic;

/**
 * 如下的代码在当前测试环境下：(i7-4800MQ，JDK11，Linux(Arch)) ，控制台的输出，查找期望的 serialNumber 的最大值 999，
 * 在运行多次的情况下，证明，没有得到期望的最大值 999，可见，在不加锁的情况下，不能保证 serialNumber++ 操作的原子性！
 * <br/>
 * <strong>不能保证原子性：</strong> 0 号线程读取到 serialNumber 的值为 0，修改进行 serialNumber++ 操作，在数据还没有写回到内存的时候，
 * 1 号线程也读取到 serialNumber 值为 0，同样进行 serialNumber++ 操作，然后 0 号线程将 serialNumber++ 操作后的值(1)，写入到内存，
 * 1 号线程也将 serialNumber++ 后的值(也为 1) 写入到内存，则此时 2 号线程读取 serialNumber 的值为 1，
 * 故相当于丢失了一次 serialNumber++ 操作(两次 serialNumber++ 操作，实际 serialNumber 的值增加 2).
 * <br/>
 * <strong>是否可以加上 volatile 来解决这个问题？ 不能！</strong> volatile 只能保证内存可见性，同样会存在两个线程同时读取到相同的 serialNumber 的情况，
 * 然后同时进行 serialNumber++ 操作，与上述情况相同！@see {@link TestAtomicWithoutAtomicButWithVolatile}
 *
 * @author gldwolf
 */
public class TestAtomicWithoutAtomic {
    public static void main(String[] args) {
        AtomicDemoWithoutAtomic adwa = new AtomicDemoWithoutAtomic();
        for (int i = 0; i < 1000; i++) {
            new Thread(adwa, "thread-" + i).start();
        }
    }
}

class AtomicDemoWithoutAtomic implements Runnable {
    private int serialNumber = 0;

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
