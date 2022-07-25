package com.tianci.springframework.test;

import cn.bugstack.springframework.beans.PropertyValue;
import cn.bugstack.springframework.beans.PropertyValues;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.config.BeanReference;
import cn.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.bugstack.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import cn.bugstack.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import cn.bugstack.springframework.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.core.io.ClassPathResource;
import cn.bugstack.springframework.core.io.DefaultResourceLoader;
import cn.bugstack.springframework.core.io.Resource;
import cn.bugstack.springframework.core.io.ResourceLoader;
import cn.hutool.core.io.IoUtil;
//import com.tianci.springframework.test.bean.UserDao;
import com.tianci.springframework.test.bean.IUserDao;
import com.tianci.springframework.test.bean.ProxyBeanFactory;
import com.tianci.springframework.test.bean.UserService;
import com.tianci.springframework.test.event.CustomEvent;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

import java.io.IOException;
import java.io.InputStream;

public class ApiTest {
    @Test
    public void testResourceLoader() throws IOException {
//        InputStream inputStream = new ClassPathResource("classpath:important.properties").getInputStream();
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        //从当前所在包下的resources开始读起
//        Resource resource = resourceLoader.getResource("classpath:important.properties");
//        Resource resource = resourceLoader.getResource("classpath:testProperty.properties");
        Resource resource = resourceLoader.getResource("classpath:testProperty/testProperty.properties");
        InputStream inputStream = resource.getInputStream();
        System.out.println(IoUtil.readUtf8(inputStream));
    }

//    @Test
//    public void testBeanFactory(){
//        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
//        defaultListableBeanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));
//
//        PropertyValues propertyValues = new PropertyValues();
//        propertyValues.addPropertyValue(new PropertyValue("uId", "10001"));
//        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));
//
//        // 4. UserService 注入bean
//        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
//        defaultListableBeanFactory.registerBeanDefinition("userService", beanDefinition);
//
//        // 5. UserService 获取bean
//        UserService userService = (UserService) defaultListableBeanFactory.getBean("userService");
//        String result = userService.queryUserInfo();
//        System.out.println("测试结果：" + result);
//    }

    @Test
    public void testXml(){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");

        UserService userService = beanFactory.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
    }

    @Test
    public void testContext(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springPostProcessor.xml");
        applicationContext.registerShutdownHook();
        UserService userService = applicationContext.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
    }

    @Test
    public void testAware(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springPostProcessor.xml");
//        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springWithoutProxy.xml");
        applicationContext.registerShutdownHook();

        //If the scope of UserService bean is singleton: will initialize bean once:
        //print:
        //Bean Class Loader in UserService:jdk.internal.loader.ClassLoaders$AppClassLoader@7b3300e5
        //BeanName in UserService:userService
        //Execute: init userService method
        //If the scope is prototype, will print the above output twice (once when "new ClassPathXmlApplicationContext" and
        //another once when "getBean()")
        UserService userService = applicationContext.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("result：" + result);
        System.out.println("ApplicationContextAware："+userService.getApplicationContext());
        System.out.println("BeanFactoryAware："+userService.getBeanFactory());
    }


    @Test
    public void testNULL_OBJECT(){
        Object obj = new Object();
        Object obj2 = new Object();
        System.out.println("obj == obj2? "+(obj == obj2) );

        String str1 = "hello";
        String str2 = "hello";
        String str3 = new String("hello");
        System.out.println("str1 == str2 ?"+(str1 == str2));        // true
        System.out.println("str1 == str3 ?"+(str1 == str3));        // false
        System.out.println("str1 equals str3 ?"+(str1.equals(str3)));   // true
    }


    @Test
    public void testPrototype(){
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springPostProcessor.xml");
        applicationContext.registerShutdownHook();

        // 2. 获取Bean对象调用方法
        UserService userService01 = applicationContext.getBean("userService", UserService.class);
        UserService userService02 = applicationContext.getBean("userService", UserService.class);

        // 3. 配置 scope="prototype/singleton"
        System.out.println("userService01:"+userService01);
        System.out.println("userService02:"+userService02);

        // 4. 打印十六进制哈希
        System.out.println(userService01 + " 十六进制哈希：" + Integer.toHexString(userService01.hashCode()));
        System.out.println(ClassLayout.parseInstance(userService01).toPrintable());

    }

    @Test
    public void testDraft(){
        String s = "abcde";
        System.out.println(s.contains("h"));
        System.out.println(s.toCharArray());
        System.out.println(s.charAt(2) == 'c');
    }

    @Test
    public void testProxy() throws Exception {
        ProxyBeanFactory factory = new ProxyBeanFactory();
        IUserDao proxy = factory.getObject();
        System.out.println(proxy.queryUserName("10001"));
        UserService service = new UserService();
        service.setuId("10001");
        service.setUserDao(proxy);
        service.setCompany("Tencent");
        service.setLocation("Shenzheng");
        System.out.println(service.queryUserInfo());
    }

    @Test
    public void test_factory_bean() {
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springProxy.xml");
        applicationContext.registerShutdownHook();
        // 2. 调用代理方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }

    @Test
    public void testCglib(){
        //java.lang.IllegalArgumentException: Unsupported class file major version 59
        //solution: Preferences -> Java Compiler -> v4's target bytecode version change from 15 to 8
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ProxyBeanFactory.class);
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
        Object bean = enhancer.create();
        System.out.println(bean);
    }

    @Test
    public void testScope(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springTest.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, 9083210983L, "Success！"));
        applicationContext.registerShutdownHook();

        UserService userService = applicationContext.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("result：" + result);
        System.out.println("ApplicationContextAware："+userService.getApplicationContext());
        System.out.println("BeanFactoryAware："+userService.getBeanFactory());
    }

    @Test
    public void testEvent(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springTest.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, 9083210983L, "Success！"));

        applicationContext.registerShutdownHook();
    }

}
