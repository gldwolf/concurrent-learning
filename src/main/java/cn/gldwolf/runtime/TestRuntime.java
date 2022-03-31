package cn.gldwolf.runtime;

public class TestRuntime {
    public static void main(String[] args) throws InterruptedException {
        final Thread shutdownHook = new Thread(() -> {
            try {
                Thread.sleep(200);
                System.out.println("Application now is stopped!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        System.out.println("Application is running!");
        Thread.sleep(10000);
    }
}
