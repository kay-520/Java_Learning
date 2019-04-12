/**
 * @author: Wangmh
 * @date: 2019/4/11
 * @time: 16:21
 */
public class ThreadDemo3 {
    public static void main(String[] args) {
        System.out.println("-----多线程创建开始-----");
        Thread thread =new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i< 10; i++) {
                    System.out.println("i:" + i);
                }
            }
        });
        thread.start();
        System.out.println("-----多线程创建结束-----");
    }
}
