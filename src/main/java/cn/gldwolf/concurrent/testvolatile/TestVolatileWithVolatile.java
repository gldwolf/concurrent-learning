package cn.gldwolf.concurrent.testvolatile;

/**
 * 在 shutdown 变量前加上 volatile 关键字，会强制线程(此例中是 son-thread)在修改该变量后，将变量的值写回主内存，而不再是仅仅写入到缓存中，
 * 而在另一个线程(此例中是 main) 读取 shutdown 变量时会强制其从主内存中读取该变量的值，而不是从自己的缓存中读取该值，
 * 这就保证了 shutdown 变量值变化后，其它线程可以及时感知，这就是内存可见性.
 * <br/>
 * volatile 关键字解决了内存可见性错误，但是解决不了原子性。
 * <ul>
 *     <li>volatile 不具备互斥性</li>
 *     <li>volatile 不能保证变量的原子性</li>
 * </ul>
 * @see cn.gldwolf.concurrent.testatomic.TestAtomicWithoutAtomic TestAtomicWithoutAtomic
 * @see cn.gldwolf.concurrent.testatomic.TestAtomicWithoutAtomicButWithVolatile TestAtomicWithoutAtomicButWithVolatile
 *
 * @author gldwolf
 */
public class TestVolatileWithVolatile {
    public static void main(String[] args) {
        VolatileDemoWithVolatile vdwv = new VolatileDemoWithVolatile();
        new Thread(vdwv, "son-thread").start();

        while (true) {
            if (vdwv.isShutdown()) {
                break;
            }
        }
    }
}

class VolatileDemoWithVolatile implements Runnable {
    /**
     * volatile 可以保证内存可见性，但是不能保证对 shutdown 操作的原子性。<br/>
     * <strong>不能保证原子性：</strong> 可以有多个线程同时读取到该值，并对其进行操作。
     * 比如对其进行取反操作时，两个线程同时读取到其值为 false，同时在自己的 cpu 上进行取反操作，
     * 得到相同的值为 true，同时写回到主存 true，而不是期望的 false -> true -> false
     */
    private volatile boolean shutdown = false;

    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.shutdown = true;
        System.out.println("isShutdown = " + shutdown);
    }
}
