package cn.gldwolf.concurrent.testvolatile;

/**
 * <strong>内存可见性：</strong>是指当某个线程正在使用对个变量的状态，而另一个线程会修改该变量的状态，
 * 需要确保当另一个线程修改了该属性的状态后，其它线程能够及时观察到状态的变化。 <br/>
 * <strong>可见性错误：</strong>在不使用 volatile 关键字的情况下，主线程读取到初始的 isShutdown 的值为 false，
 * 在后面的循环判断时，会一直使用主线程所在的 CPU core 的高速缓存，在其它线程更新了这个值以后，
 * 主线程也不会读取主内存中的数据，其它线程也不会将更新值刷新到主内存，这就是内存可见性错误! <br/>
 * <strong>解决方案：</strong>
 * <ol>
 *     <li>在 isShutdown() 方法上加 synchronized 关键字，但是这种方法太重量级了</li>
 *     <li>在 shutdown 变量前加上 volatile 关键字: @see {@link TestVolatileWithVolatile}</li>
 * </ol>
 *
 * @author gldwolf
 */
public class TestVolatileWithoutVolatile {

    public static void main(String[] args) {
        VolatileDemoWithoutVolatile vd = new VolatileDemoWithoutVolatile();
        new Thread(vd, "son-thread").start();

        while (true) {
            // 在 son-thread 线程将 isShutdown 值设置为 true 后，
            // 主线程并不会感知到该值的变化，一直会使用自己高速缓存中的数据，所以值一直为 true，循环不会退出
            if (vd.isShutdown()) {
                break;
            }
        }
    }

}

class VolatileDemoWithoutVolatile implements Runnable {
    private boolean shutdown = false;

    public boolean isShutdown() {
        return this.shutdown;
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
