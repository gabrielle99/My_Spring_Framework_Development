package cn.bugstack.springframework.aop;


import java.lang.reflect.Method;

public interface MethodBeforeAdvice extends BeforeAdvice {
    /**
     * Callback before a given method is invoked
     */
    void before(Method method, Object[] args, Object target) throws Throwable;
}
