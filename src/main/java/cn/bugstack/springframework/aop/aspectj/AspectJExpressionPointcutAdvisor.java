package cn.bugstack.springframework.aop.aspectj;

import cn.bugstack.springframework.aop.Pointcut;
import cn.bugstack.springframework.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    //切面
    private AspectJExpressionPointcut pointcut;

    //具体的拦截方法
    private Advice advice;

    //表达式
    private String expression;

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    @Override
    public Pointcut getPointcut() {
        if (null == this.pointcut){
            this.pointcut = new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }
}
