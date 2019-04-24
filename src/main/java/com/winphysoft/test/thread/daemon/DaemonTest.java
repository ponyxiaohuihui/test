package thread.daemon;

public class DaemonTest {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
            }
        });
        Thread thread = new Thread(){
            public void run(){

                Thread child = new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            System.out.println("child1");
                            Thread.sleep(1000);
                            System.out.println("child2");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                child.setDaemon(true);
                child.start();
                try {
                    Thread.sleep(1000);
                    System.out.println("thread");
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();
        System.out.println("finish");

    }
}
