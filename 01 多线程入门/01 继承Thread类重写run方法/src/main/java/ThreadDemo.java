/**
 * @author: Wangmh
 * @date: 2019/4/11
 * @time: 16:08
 */
public class ThreadDemo {
    public static void main(String[] args) {
        System.out.println("--多线程创建开始--");
        //创建线程
        CreateThread thread=new CreateThread();
        //开始执行线程 注意：开启线程不是调用run方法，而是start方法
        System.out.println("--多线程创建启动--");
        thread.start();
        System.out.println("--多线程创建结束--");
//调用start方法后，代码并没有从上往下执行，而是有一条新的执行分之
//        --多线程创建开始--
//                --多线程创建启动--
//        i:0
//        i:1
//        i:2
//        i:3
//        i:4
//        i:5
//        i:6
//        i:7
//        i:8
//        i:9
//                --多线程创建结束--
//        i:0
//        i:1
//        i:2
//        i:3
//        i:4
//        i:5
//        i:6
//        i:7
//        i:8
//        i:9
    }
}
