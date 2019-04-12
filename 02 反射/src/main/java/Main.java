import com.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author: Wangmh
 * @date: 2019/4/12
 * @time: 10:22
 * @Dep: 反射机制
 */
public class Main {
    public static void main(String[] args) throws Exception {
        //1.反射机制获取类的三种方法
        Class c1 = Class.forName("com.User");
        Class c2 = User.class;//java中每个类型都有class 属性.
        User user = new User();
        Class c3 = user.getClass();//c3是运行时类

        //2.反射创建对象的方式
        Class<?> forName = Class.forName("com.User");
        //创建此Class对象所表示的类的一个新实例 调用了User的无参构造
        Object newInstance = forName.newInstance();
        User user1 = (User) newInstance;
        //实例化需要有参构造函数
        Class<?> forName1 = Class.forName("com.User");
        Constructor<?> constructor = forName1.getConstructor(Integer.class, String.class, Integer.class);
        User user2 = (User) constructor.newInstance(1, "明欢", 1);

        //3.反射创建API
        //3.1 获取该类所有方法
        Method[] methods = forName1.getDeclaredMethods();
        //3.2 获取该类的返回值
        Class returnType = methods[0].getReturnType();
        //3.3 获取传入参数
        Class[] classes=methods[0].getParameterTypes();
        //3.4 获取该类的所有字段
        Field[] fields=forName1.getDeclaredFields();
        //3.5 允许访问私有成员并赋值
        fields[0].setAccessible(true);
        fields[0].set(user1,11);
    }
}
