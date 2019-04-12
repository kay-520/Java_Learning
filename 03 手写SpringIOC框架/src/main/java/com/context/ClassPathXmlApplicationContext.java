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
