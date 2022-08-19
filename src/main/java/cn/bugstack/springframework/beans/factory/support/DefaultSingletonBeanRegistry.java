package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.DisposableBean;
import cn.bugstack.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    protected static final Object NULL_OBJECT = new Object();
    private Map<String, Object> singletonObjects = new HashMap<>();
    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    public void registerDisposableBean(String beanName, DisposableBean disposableBean){
        disposableBeans.put(beanName, disposableBean);
    }

    //singleton 才要destroy
    public void destroySingletons(){
        Set<String> keySet = disposableBeans.keySet();
        Object[] disposalBeansArray = keySet.toArray();

        for (int i = disposalBeansArray.length - 1; i>=0; i--){
            Object beanName = disposalBeansArray[i];
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Exception occur for destroy method of bean named: "+beanName.toString(), e);
            }
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }

    public void addSingleton(String beanName, Object beanObj){
        singletonObjects.put(beanName, beanObj);
    }
}
