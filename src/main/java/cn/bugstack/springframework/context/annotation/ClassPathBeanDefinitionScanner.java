package cn.bugstack.springframework.context.annotation;

import cn.bugstack.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.support.BeanDefinitionRegistry;
import cn.bugstack.springframework.stereotype.Component;
import cn.hutool.core.util.StrUtil;

import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    /**
     * 在 doScan 中除了获取到扫描的类信息以后，还需要获取 Bean 的作用域和类名，如果不配置类名基本都是把首字母缩写
     */
    public void doScan(String... basePackages){
        for (String basePackage:basePackages){
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition beanDefinition : candidates){
                String scope = resolveBeanScope(beanDefinition);
                if (StrUtil.isNotEmpty(scope)){
                    beanDefinition.setScope(scope);
                }
                registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
            }
        }

        // 注册处理注解的 BeanPostProcessor（@Autowired、@Value）
        registry.registerBeanDefinition("cn.bugstack.springframework.context.annotation.internalAutowiredAnnotationProcessor", new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    private String resolveBeanScope(BeanDefinition beanDefinition){
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (null != scope){
            return scope.value();
        }
        return StrUtil.EMPTY;
    }

    private String determineBeanName(BeanDefinition beanDefinition){
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);
        String name = component.value();
        if (StrUtil.isEmpty(name)){
            name = StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        return name;
    }
}
