package com.tianci.springframework.test.event;

import cn.bugstack.springframework.context.ApplicationListener;

import java.util.Date;

public class CustomEventListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("------------Custom Event------------");
        System.out.println("Receive：" + event.getSource() + "msg;Time：" + new Date());
        System.out.println("Msg：" + event.getId() + ":" + event.getMsg());
    }
}
