package com.tianci.springframework.test.bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao implements IUserDao{
    private static Map<String, String> hashMap = new HashMap<>();

//    static {
//        hashMap.put("10001", "小傅哥");
//        hashMap.put("10002", "八杯水");
//        hashMap.put("10003", "阿毛");
//    }

    public void initDataMethod(){
        System.out.println("Execute：init-method in UserDao");
        hashMap.put("10001", "tianci");
        hashMap.put("10002", "gabrielle");
        hashMap.put("10003", "lily");
    }

    public void destroyDataMethod(){
        System.out.println("Execute：destroy-method in UserDao");
        hashMap.clear();
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }
}
