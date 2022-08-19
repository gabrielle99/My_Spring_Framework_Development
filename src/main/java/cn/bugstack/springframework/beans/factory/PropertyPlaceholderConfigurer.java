package cn.bugstack.springframework.beans.factory;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.PropertyValue;
import cn.bugstack.springframework.beans.PropertyValues;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.config.BeanFactoryPostProcessor;
import cn.bugstack.springframework.core.io.DefaultResourceLoader;
import cn.bugstack.springframework.core.io.Resource;
import cn.bugstack.springframework.utils.StringValueResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * 对配置文件的加载以及摘取占位符中的在属性文件里的配置
 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
    private String location;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //加载属性文件
        try {
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());

            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanName:beanDefinitionNames){
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                PropertyValues propertyValues = beanDefinition.getPropertyValues();
                for (PropertyValue propertyValue:propertyValues.getPropertyValues()){
                    Object value = propertyValue.getValue();
                    if (!(value instanceof String)){
                        continue;
                    }
//                    String str = (String) value;
//                    StringBuilder builder = new StringBuilder(str);
//                    int startIndex = str.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
//                    int endIndex = str.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
//                    if (startIndex != -1 && endIndex != -1 && startIndex < endIndex){
//                        String propKey = str.substring(startIndex+2, endIndex);
//                        String propVal = properties.getProperty(propKey);
//                        builder.replace(startIndex, endIndex+1, propVal);
//                        propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), builder.toString()));
//                    }
                    value = resolvePlaceholder((String) value, properties);
                    propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), value));
                }
            }

            //向容器中添加字符串解析器，供解析@Value注解使用
            StringValueResolver resolver = new PlaceholderResolvingStringValueResolver(properties);
            // 把属性值写入到 AbstractBeanFactory 的 embeddedValueResolvers 中
            beanFactory.addEmbeddedValueResolver(resolver);
        } catch (IOException e) {
            throw new BeansException("Could not load properties:"+e);
        }
    }

    private String resolvePlaceholder(String value, Properties properties){
        String str = value;
        StringBuilder builder = new StringBuilder(str);
        int startIndex = str.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        int endIndex = str.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex){
            String propKey = str.substring(startIndex+2, endIndex);
            String propVal = properties.getProperty(propKey);
            builder.replace(startIndex, endIndex+1, propVal);
        }
        return builder.toString();
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver{

        private final Properties properties;

        public PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String resolveStringValue(String strVal) {
            //类名.this:内部类调用外部类的对象
            return PropertyPlaceholderConfigurer.this.resolvePlaceholder(strVal, properties);
        }
    }
}
