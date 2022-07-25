package cn.bugstack.springframework.beans.factory.config;

public class BeanReference {
    String beanName;

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
