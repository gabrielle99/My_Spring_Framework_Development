package com.tianci.springframework.test;

import cn.bugstack.springframework.aop.AdvisedSupport;
import cn.bugstack.springframework.aop.TargetSource;
import cn.bugstack.springframework.aop.aspectj.AspectJExpressionPointcut;
import cn.bugstack.springframework.aop.framework.Cglib2AopProxy;
import cn.bugstack.springframework.aop.framework.JdkDynamicAopProxy;
import cn.bugstack.springframework.beans.PropertyValue;
import cn.bugstack.springframework.beans.PropertyValues;
import cn.bugstack.springframework.beans.factory.PropertyPlaceholderConfigurer;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.config.BeanReference;
import cn.bugstack.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy;
import cn.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.bugstack.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import cn.bugstack.springframework.beans.factory.support.InstantiationStrategy;
import cn.bugstack.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import cn.bugstack.springframework.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.core.io.ClassPathResource;
import cn.bugstack.springframework.core.io.DefaultResourceLoader;
import cn.bugstack.springframework.core.io.Resource;
import cn.bugstack.springframework.core.io.ResourceLoader;
import cn.hutool.core.io.IoUtil;
//import com.tianci.springframework.test.bean.UserDao;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.tianci.springframework.test.bean.*;
import com.tianci.springframework.test.event.CustomEvent;
import com.tianci.springframework.test.self.User;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public class ApiTest {
    public static int test01(){
        try {
            return 0;
        } finally {
            System.out.println("finally ...");
        }
    }
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

        applicationContext.registerShutdownHook();
        applicationContext.publishEvent(new CustomEvent(applicationContext, 9083210983L, "Success！"));
    }

    @Test
    public void testFinally(){
        System.out.println(test01());
    }

    @Test
    public void testAssert(){
        String str = "abc";
        Assert.isNull(str);     //hutool 库
        System.out.println("After Assert...");
    }


    @Test
    public void testCglib01() throws NoSuchMethodException {
        BeanDefinition beanDefinition = new BeanDefinition(User.class);
//        Constructor constructor = beanDefinition.getBeanClass().getConstructor();
//        System.out.println("constructor:"+constructor);
        Object[] args = null;
        InstantiationStrategy strategy = new CglibSubclassingInstantiationStrategy();
        System.out.println(strategy.instantiate(beanDefinition,"user", null, args));
    }

    @Test
    public void testCglib02(){
        Constructor constructorToUse = null;
        BeanDefinition beanDefinition = new BeanDefinition(UserDao.class);
        Constructor<?>[] constructors = beanDefinition.getBeanClass().getDeclaredConstructors();
        Object[] args = null;
        for (Constructor constructor : constructors){
            if (null != args && constructor.getParameterTypes().length == args.length){
                constructorToUse = constructor;
                break;
            }
        }
        InstantiationStrategy strategy = new CglibSubclassingInstantiationStrategy();
        System.out.println(strategy.instantiate(beanDefinition, "userDao", constructorToUse, args));
    }

    @Test
    public void testMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = User.class.getMethod("getName");
        User user = new User();
        user.setName("cc");
        System.out.println((String)method.invoke(user));
    }

    @Test
    public void testAop() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* com.tianci.springframework.test.bean.UserService.*(..))");
        Class<UserService> clazz = UserService.class;
        Class<UserDao> userDaoClass = UserDao.class;
        Method method = clazz.getMethod("queryUserInfo");
        Method method1 = userDaoClass.getMethod("initDataMethod");

        System.out.println("class match:"+pointcut.matches(clazz));
        System.out.println("class match:"+pointcut.matches(userDaoClass));
        System.out.println("method match:"+pointcut.matches(method, clazz));
        System.out.println("method match:"+pointcut.matches(method1, clazz));
    }

    @Test
    public void testDynamic(){
        IUserService userService = new UserService();
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* com.tianci.springframework.test.bean.IUserService.*(..))"));
        advisedSupport.setMethodInterceptor(new UserServiceInterceptor());

        IUserService proxy = (IUserService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        System.out.println("Aop testing result:"+proxy.register());
    }

    @Test
    public void testCglibAop(){
        IUserService userService = new UserService();
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* com.tianci.springframework.test.bean.IUserService.*(..))"));
        advisedSupport.setMethodInterceptor(new UserServiceInterceptor());

        IUserService proxy = (IUserService) new Cglib2AopProxy(advisedSupport).getProxy();
        System.out.println("Cglib Aop testing result:"+proxy.register());
    }

    @Test
    public void test_aop(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springTest.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
//        System.out.println("测试结果: "+userService.queryUserInfo());
        System.out.println("测试结果: "+userService.register());
    }

    @Test
    public void testResource() throws IOException {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:springTest.xml");
        Properties properties = new Properties();
        properties.load(resource.getInputStream());
        System.out.println(properties.getProperty("<property"));

        System.out.println("Split to array:"+ StrUtil.splitToArray("cn.bugstack.springframework.test.bean,cn.test", ',')[1]);
    }

    @Test
    public void testProperty(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-property.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService);
    }

    @Test
    public void test_scan() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-scan.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.register());
    }

    @Test
    public void test_scan2() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-scan.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserAndToken());
    }


}
