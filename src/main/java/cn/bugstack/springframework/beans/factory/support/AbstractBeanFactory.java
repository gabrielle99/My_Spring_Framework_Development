package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.BeanFactory;
import cn.bugstack.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.bugstack.springframework.beans.factory.FactoryBean;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.config.BeanPostProcessor;
import cn.bugstack.springframework.beans.factory.config.ConfigurableBeanFactory;
import cn.bugstack.springframework.utils.ClassUtils;
import cn.bugstack.springframework.utils.StringValueResolver;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();
    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();
    @Override
    public Object getBean(String beanName) throws BeansException {
        return doGetBean(beanName, null);
    }

    @Override
    public Object getBean(String beanName, Object... args) throws BeansException {
        return doGetBean(beanName, args);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> requiredType) throws BeansException {
        return (T)getBean(beanName);
    }

    protected <T> T doGetBean(final String beanName, final Object[] args) throws BeansException{
        Object bean = getSingleton(beanName);
        if (null!= bean){
//            return (T) bean;
            return (T) getObjectForBeanInstance(bean, beanName);
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        Object beanInstance = (T)createBean(beanName, beanDefinition, args);
        return (T)getObjectForBeanInstance(beanInstance, beanName);
    }

    private Object getObjectForBeanInstance(Object beanInstance, String beanName){
        if (!(beanInstance instanceof FactoryBean)){
            return beanInstance;
        }

        // 处理代理类
        Object obj = getCachedObjectForFactoryBean(beanName);
        if (obj == null){
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            obj = getObjectFromFactoryBean(factoryBean, beanName);
        }
        return obj;
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver resolver){
        this.embeddedValueResolvers.add(resolver);
    }

    @Override
    public String resolveEmbeddedValue(String value){
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers){
            result = resolver.resolveStringValue(result);
        }
        return result;
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }
}
