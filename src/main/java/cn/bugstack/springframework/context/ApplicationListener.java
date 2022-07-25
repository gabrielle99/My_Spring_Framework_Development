package cn.bugstack.springframework.context;

import java.util.EventListener;

public interface ApplicationListener<T extends ApplicationEvent> extends EventListener {
    void onApplicationEvent(T event);
}
