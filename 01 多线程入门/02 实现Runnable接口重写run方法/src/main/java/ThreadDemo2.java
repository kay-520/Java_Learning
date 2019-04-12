/**
 * @author: Wangmh
 * @date: 2019/4/11
 * @time: 16:15
 */
public class ThreadDemo2 {
    public static void main(String[] args) {
        System.out.println("--多线程创建开始--");
        //1.创建一个线程
        CreateRunnable runnable = new CreateRunnable();
        //2.开始执行线程，开启线程不是调用run方法，而是start方法
        System.out.println("-----多线程创建启动-----");
        Thread thread = new Thread(runnable);
        thread.start();
        System.out.println("-----多线程创建结束-----");
    }
}
