package com.tianci.springframework.test.self;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class MyTest01 {
    @Test
    public void testParameterizedType(){
        Class<ReflectTestBean> cls = ReflectTestBean.class;
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields){
            System.out.println(field.getName()+" Generic Type:"+field.getGenericType());
            System.out.println(field.getName()+" is generic type？"+(field.getGenericType() instanceof ParameterizedType));
        }
        System.out.println("----------------");
        for (Field field : fields){
            if (field.getGenericType() instanceof ParameterizedType){
                ParameterizedType type = (ParameterizedType) field.getGenericType();
                System.out.print(field.getName()+"的属性为："+type.getTypeName());
                Type[] types = type.getActualTypeArguments();
                for (Type t: types){
                    System.out.println(", 真实的泛型类型为："+t.getTypeName());
                }
            }
        }
    }
}
