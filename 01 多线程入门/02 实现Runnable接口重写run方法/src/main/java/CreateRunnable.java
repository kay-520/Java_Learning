/**
 * @author: Wangmh
 * @date: 2019/4/11
 * @time: 16:14
 */
public class CreateRunnable implements Runnable {

    public void run() {
        for (int i = 0; i< 10; i++) {
            System.out.println("i:" + i);
        }
    }
}
