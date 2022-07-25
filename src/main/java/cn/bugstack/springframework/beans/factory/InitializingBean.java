package cn.bugstack.springframework.beans.factory;



public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
