/**
 * @author: Wangmh
 * @date: 2019/4/11
 * @time: 16:43
 */
public class JoinThread implements Runnable {
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + "---i:" + i);
        }
    }
}
