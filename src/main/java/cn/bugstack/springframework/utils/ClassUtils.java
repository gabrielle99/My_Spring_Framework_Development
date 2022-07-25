package cn.bugstack.springframework.utils;

import cn.bugstack.springframework.beans.BeansException;

public class ClassUtils {
    public static ClassLoader getDefaultClassLoader(){
        ClassLoader cl = null;
        try{
            cl = Thread.currentThread().getContextClassLoader();
        }catch (Exception e){
            throw new BeansException("class loader fail:"+e);
        }
        if (cl == null){
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }

    /**
     * Check whether the specified class is a CGLIB-generated class.
     * @param clazz the class to check
     */
    public static boolean isCglibProxyClass(Class<?> clazz) {
        return (clazz != null && isCglibProxyClassName(clazz.getName()));
    }

    /**
     * Check whether the specified class name is a CGLIB-generated class.
     * 产生的代理文件都有$$符号，可用于判断是否是代理类
     * @param className the class name to check
     */
    public static boolean isCglibProxyClassName(String className) {
        return (className != null && className.contains("$$"));
    }
}
