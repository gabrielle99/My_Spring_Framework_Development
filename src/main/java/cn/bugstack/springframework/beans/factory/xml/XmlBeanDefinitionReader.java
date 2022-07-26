package cn.bugstack.springframework.beans.factory.xml;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.PropertyValue;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.config.BeanReference;
import cn.bugstack.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import cn.bugstack.springframework.beans.factory.support.BeanDefinitionRegistry;
import cn.bugstack.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import cn.bugstack.springframework.core.io.Resource;
import cn.bugstack.springframework.core.io.ResourceLoader;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            try(InputStream inputStream = resource.getInputStream()) {
                doLoadBeanDefinitions(inputStream);
            }
        }catch (ClassNotFoundException | IOException | DocumentException e){
            throw new BeansException("IOException parsing XML document from "+resource,e);
        }

    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {
        for (Resource resource : resources){
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    public void loadBeanDefinitions(String... locations) throws BeansException{
        for (String location : locations){
            loadBeanDefinitions(location);
        }
    }

//    protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException{
//        Document doc = XmlUtil.readXML(inputStream);
//        Element root = doc.getDocumentElement();
////        System.out.println(root.getTagName());
//        NodeList childLists = root.getChildNodes();
//
////        for (int i = 0; i< childLists.getLength(); i++){
////            System.out.println(childLists.item(i));
////        }
//        for (int i = 0; i<childLists.getLength(); i++){
//            if (!(childLists.item(i) instanceof Element)) {
//                continue;
//            }
//            if (!"bean".equals(childLists.item(i).getNodeName())){
//                continue;
//            }
//            Element bean = (Element) childLists.item(i);
//            String id = bean.getAttribute("id");
//            String name = bean.getAttribute("name");
//            String className = bean.getAttribute("class");
//
//            String initMethodName = bean.getAttribute("init-method");
//            String destroyMethodName = bean.getAttribute("destroy-method");
//            String beanScope = bean.getAttribute("scope");
//
//            Class<?> clazz = Class.forName(className);
//            String beanName = StrUtil.isNotEmpty(id)? id:name;
//            if (StrUtil.isEmpty(beanName)) {
//                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
//            }
//
//            // Define BeanDefinition
//            BeanDefinition beanDefinition = new BeanDefinition(clazz);
//            beanDefinition.setInitMethodName(initMethodName);
//            beanDefinition.setDestroyMethodName(destroyMethodName);
//
//            if (StrUtil.isNotEmpty(beanScope)){
//                beanDefinition.setScope(beanScope);
//            }
//
////            System.out.println("childlists.len="+childLists.getLength());
////            System.out.println("bean.getChildNodes().getLength="+bean.getChildNodes().getLength());
//
//            // Read Properties
////            for (int j = 0; j < childLists.getLength(); j++){
//            for (int j = 0; j < bean.getChildNodes().getLength(); j++){
//                if (!(bean.getChildNodes().item(j) instanceof Element)){
//                    continue;
//                }
//                if (!"property".equals(bean.getChildNodes().item(j).getNodeName())){
//                    continue;
//                }
//                Element property = (Element) bean.getChildNodes().item(j);
//                String attrName = property.getAttribute("name");
//                String attrVal = property.getAttribute("value");
//                String attrRef = property.getAttribute("ref");
//                Object value = StrUtil.isNotEmpty(attrRef)? new BeanReference(attrRef):attrVal;
//                PropertyValue propertyValue = new PropertyValue(attrName, value);
//                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
//            }
//            if (getRegistry().containsBeanDefinition(beanName)){
//                throw new BeansException("Duplicate bean name "+beanName+" is not allowed.");
//            }
//            getRegistry().registerBeanDefinition(beanName, beanDefinition);
//        }
//
//    }


    protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException, DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(inputStream);
        org.dom4j.Element root = doc.getRootElement();

        // 解析 context:component-scan 标签，扫描包中的类并提取相关信息，用于组装 BeanDefinition
        org.dom4j.Element component_scan = root.element("component-scan");
        if (null != component_scan){
            String scanPath = component_scan.attributeValue("base-package");
            if (StrUtil.isEmpty(scanPath)){
                throw new BeansException("The value of base-package cannot be empty.");
            }
            scanPackage(scanPath);
        }

        List<org.dom4j.Element> beanList = root.elements("bean");

        for (Element bean:beanList){
            String id = bean.attributeValue("id");
            String name = bean.attributeValue("name");
            String className = bean.attributeValue("class");

            String initMethodName = bean.attributeValue("init-method");
            String destroyMethodName = bean.attributeValue("destroy-method");
            String beanScope = bean.attributeValue("scope");

            Class<?> clazz = Class.forName(className);
            String beanName = StrUtil.isNotEmpty(id)? id:name;
            if (StrUtil.isEmpty(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            // Define BeanDefinition
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            beanDefinition.setInitMethodName(initMethodName);
            beanDefinition.setDestroyMethodName(destroyMethodName);

            if (StrUtil.isNotEmpty(beanScope)){
                beanDefinition.setScope(beanScope);
            }

            List<Element> propertyList = bean.elements("property");

            for (Element property : propertyList){
                String attrName = property.attributeValue("name");
                String attrVal = property.attributeValue("value");
                String attrRef = property.attributeValue("ref");
                Object value = StrUtil.isNotEmpty(attrRef)? new BeanReference(attrRef):attrVal;
                PropertyValue propertyValue = new PropertyValue(attrName, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            if (getRegistry().containsBeanDefinition(beanName)){
                throw new BeansException("Duplicate bean name "+beanName+" is not allowed.");
            }
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }

    }

    private void scanPackage(String scanPath){
        String[] basePackages = StrUtil.splitToArray(scanPath, ',');
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        scanner.doScan(basePackages);
    }
}
