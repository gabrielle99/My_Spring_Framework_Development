package cn.bugstack.springframework.stereotype;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {       // 用于配置到 Class 类上
    String value() default "";
}
