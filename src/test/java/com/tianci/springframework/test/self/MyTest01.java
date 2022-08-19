package com.tianci.springframework.test.self;

import org.junit.Test;

import java.io.*;
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

    @Test
    public void testObj() throws IOException {
        File file = new File("/Users/tianci/Downloads/small-spring-main/small-spring-learn/small-spring-learn-v4/src/test/resources/testFile/file01.txt");
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        CustomObj obj = new CustomObj("cc", 100);
//        obj.setPrice(100);
        System.out.println(obj);
        outputStream.writeObject(obj);
        outputStream.close();
    }

    /**
     * 装饰器模式
     * 装饰器适用场景：我有一个对象，但是这个对象的功能不能使我满意（咖啡太苦了），我就拿装饰器给他装饰一下（给咖啡加糖）。
     */
    @Test
    public void testDecDesign(){
        Coffee coffee = new BlackCoffee();
        coffee = new SugarCoffeeDecorator(coffee);
        Coffee coffee1 = coffee;
        coffee.printMaterial();
        System.out.println("coffee:"+coffee);
        System.out.println("coffee1:"+coffee1);
    }

    /**
     * 代理模式（静态代理）
     *
     * 约好的朋友来了，要给她点一杯咖啡，你知道咖啡很苦，决定直接点一杯加了糖的咖啡给她。
     */
    @Test
    public void testProxy(){
        Coffee coffee = new CoffeeProxy();
        coffee.printMaterial();
    }

    @Test
    public void test02(){
        int result = 108;
        int temp1 = result % 10;
        System.out.println(temp1);
    }

    @Test
    public void test03(){
        ListNode l1 = new ListNode(9);
        ListNode l2 = new ListNode(1);
        ListNode l3 = l2;
        System.out.println("l2==l3? "+(l3==l2));
        System.out.println("l2 equals l3? "+l2.equals(l3));
        System.out.println("l2:"+l2.hashCode());
        System.out.println("l3:"+l3.hashCode());
        for (int i = 0; i<9; i++){
            l2.next = new ListNode(9);
            l2 = l2.next;
        }
        System.out.println("l2==l3? "+(l3==l2));
        System.out.println("l2:"+l2.hashCode());
        System.out.println("l3:"+l3.hashCode());
//        while (l3.next != null){
//            System.out.println(l3.val);
//            l3 = l3.next;
//        }

        MyTest02 test02 = new MyTest02();
        test02.addTwoNumbers(l1, l3);

    }
}
