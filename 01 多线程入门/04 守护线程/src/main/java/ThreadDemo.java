/**
 * @author: Wangmh
 * @date: 2019/4/11
 * @time: 16:33
 */
public class ThreadDemo {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    System.out.println("我是子线程...");
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
            System.out.println("我是主线程");
        }
        System.out.println("主线程执行完毕!");
    }

}
