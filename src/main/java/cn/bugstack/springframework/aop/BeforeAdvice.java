package cn.bugstack.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 * Advice 都是通过方法拦截器 MethodInterceptor 实现的。环绕 Advice 类似一个拦截器的链路，Before Advice、After advice等
 */
public interface BeforeAdvice extends Advice {
}
