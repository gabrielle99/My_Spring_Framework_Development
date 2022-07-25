package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类主要处理关于 FactoryBean 此类对象的注册操作
 * 因为既有缓存的处理也有对象的获取，所以额外提供了 getObjectFromFactoryBean 进行逻辑包装，
 * 这部分的操作方式和日常做的业务逻辑开发非常相似。从Redis取数据，如果为空就从数据库获取并写入Redis
 *
 */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry{
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    protected Object getCachedObjectForFactoryBean(String beanName){
        Object object = this.factoryBeanObjectCache.get(beanName);
        return (object!=NULL_OBJECT)?object:null;
    }

    protected Object getObjectFromFactoryBean(FactoryBean factoryBean, String beanName){
        if (factoryBean.isSingleton()){
            Object object = this.factoryBeanObjectCache.get(beanName);
            if (object == null){
                object = doGetObjectFromFactoryBean(factoryBean, beanName);
                this.factoryBeanObjectCache.put(beanName, (object != null? object:NULL_OBJECT));
            }
            return (object!= NULL_OBJECT? object:null);
        }else {
            return doGetObjectFromFactoryBean(factoryBean, beanName);
        }

    }

    private Object doGetObjectFromFactoryBean(final FactoryBean factoryBean, final String beanName){
        try {
            return factoryBean.getObject();
        } catch (Exception e){
            throw new BeansException("Factory bean throws exception on object:"+beanName, e);
        }
    }
}
