package com.tianci.springframework.test.bean;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.*;
import cn.bugstack.springframework.beans.factory.annotation.Autowired;
import cn.bugstack.springframework.beans.factory.annotation.Value;
import cn.bugstack.springframework.context.ApplicationContext;
import cn.bugstack.springframework.context.ApplicationContextAware;
import cn.bugstack.springframework.stereotype.Component;

@Component("userService")
//public class UserService implements InitializingBean, DisposableBean {
public class UserService implements IUserService,BeanNameAware, BeanClassLoaderAware, ApplicationContextAware, BeanFactoryAware{
//public class UserService{
    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;

    private String uId;
    private String company;
    private String location;
//    private UserDao userDao;
    @Autowired
    private IUserDao userDao;

    @Value("${token}")
    private String token;

    public void initServiceMethod(){
        System.out.println("Execute: init userService method");
    }

    public void destroyServiceMethod(){
        System.out.println("Execute: destroy userService method");
    }

    public String queryUserInfo() {
        return userDao.queryUserName(uId)+", Company: "+company+", Location: "+location;
    }

    public String queryUserAndToken(){
        return userDao.queryUserName("10001")+","+token;
//        return "token:"+token;
    }

    public String register(){
        return "Register user";
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

//    public UserDao getUserDao() {
//        return userDao;
//    }
//
//    public void setUserDao(UserDao userDao) {
//        this.userDao = userDao;
//    }


    public IUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("Bean Class Loader in UserService:"+classLoader);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println("BeanName in UserService:"+beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserService#token = { " + token + " }";
    }

    //    @Override
//    public void destroy() throws Exception {
//        System.out.println("UserService.destroy() method");
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("UserService afterPropertiesSet method");
//    }
}
