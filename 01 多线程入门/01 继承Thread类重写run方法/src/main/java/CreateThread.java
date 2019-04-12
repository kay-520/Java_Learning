/**
 * @author: Wangmh
 * @date: 2019/4/11
 * @time: 16:07
 */
public class CreateThread extends Thread {
    //Run方法中编写 多线程需要执行的代码
    public void run(){
        for (int i=0;i<10;i++){
            System.out.println("i:"+i);
        }
    }
}
