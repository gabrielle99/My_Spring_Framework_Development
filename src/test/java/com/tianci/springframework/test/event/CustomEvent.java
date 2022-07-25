package com.tianci.springframework.test.event;

import cn.bugstack.springframework.context.ApplicationEvent;

public class CustomEvent extends ApplicationEvent {
    private Long id;
    private String msg;

    public CustomEvent(Object source, Long id, String msg) {
        super(source);
        this.id = id;
        this.msg = msg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
