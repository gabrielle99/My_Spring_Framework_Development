package cn.bugstack.springframework.beans.factory;

/**
 * 目标：获得 Spring 框架提供的 BeanFactory、ApplicationContext、BeanClassLoader等这些能力做一些扩展框架的使用。
 *      在 Spring 框架中提供一种能感知容器操作的接口，如果谁实现了这样的一个接口，就可以获取接口入参中的各类能力。
 *
 * Aware 像标签，方便统一摘取出属于此类接口的实现类
 * generally, will be used in the judgement of instanceof
 */
public interface Aware {
}
