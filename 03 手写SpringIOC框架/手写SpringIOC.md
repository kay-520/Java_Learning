#### 1.什么是SpringIOC底层实现原理

1.读取bean的XML配置文件

2.使用beanId查找bean配置，并获取配置文件中class地址。

3.使用Java反射技术实例化对象

4.获取属性配置，使用反射技术进行赋值。

> 1.利用传入的参数获取xml文件的流,并且利用dom4j解析成Document对象
>
> 2.对于Document对象获取根元素对象<beans>后对下面的<bean>标签进行遍历,判断是否有符合的id.
>
> 3.如果找到对应的id,相当于找到了一个Element元素,开始创建对象,先获取class属性,根据属性值利用反射建立对象.
>
> 4.遍历<bean>标签下的property标签,并对属性赋值.注意,需要单独处理int,float类型的属性.因为在xml配置中这些属性都是以字符串的形式来配置的,因此需要额外处理.
>
> 5.如果属性property标签有ref属性,说明某个属性的值是一个对象,那么根据id(ref属性的值)去获取ref对应的对象,再给属性赋值.
>
> 6.返回建立的对象,如果没有对应的id,或者<beans>下没有子标签都会返回null

```java
package com.entity;

/**
 * 01 创建User实体类
 * @author: Wangmh
 * @date: 2019/4/12
 * @time: 11:04
 */
public class User {
    private String userId;
    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

```



```java
package com.context;


import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 02 使用反射技术完成Java代码
 *
 * @author: Wangmh
 * @date: 2019/4/12
 * @time: 11:05
 */
public class ClassPathXmlApplicationContext {
    private String pathXml = null;

    public ClassPathXmlApplicationContext(String pathXml) {
        this.pathXml = pathXml;
    }

    public Object getBean(String beanId) throws Exception {
        if (StringUtils.isEmpty(beanId)) {
            throw new Exception("beanId is null");
        }
        SAXReader saxReader = new SAXReader();
        Document read = saxReader.read(this.getClass().getClassLoader().getResource(pathXml));
        //获取到根节点
        Element element = read.getRootElement();
        //根节点下的所有子节点
        List<Element> elements = element.elements();
        for (Element element1 : elements) {
            //获取到节点上的属性
            String id = element1.attributeValue("id");
            if (StringUtils.isEmpty(id)) {
                continue;
            }
            if (!id.equals(beanId)){
                continue;
            }
            //使用java反射机制初始化对象
            String beanClass=element1.attributeValue("class");
            Class<?> forName =Class.forName(beanClass);
            Object newInstance=forName.newInstance();
            List<Element> list=element1.elements();
            for (Element element2:list){
                String name=element2.attributeValue("name");
                String value=element2.attributeValue("value");
                Field field=forName.getDeclaredField(name);
                field.setAccessible(true);
            }
            return newInstance;
        }
        return null;
    }
}
```

