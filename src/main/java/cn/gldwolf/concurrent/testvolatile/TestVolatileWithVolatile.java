package cn.gldwolf.concurrent.testvolatile;

/**
 * 在 shutdown 变量前加上 volatile 关键字，会强制线程(此例中是 son-thread)在修改该变量后，将变量的值写回主内存，而不再是仅仅写入到缓存中，
 * 而在另一个线程(此例中是 main) 读取 shutdown 变量时会强制其从主内存中读取该变量的值，而不是从自己的缓存中读取该值，
 * 这就保证了 shutdown 变量值变化后，其它线程可以及时感知，这就是内存可见性.
 * <br/>
 * volatile 关键字解决了内存可见性，但是解决不了原子性，
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
