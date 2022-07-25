package cn.bugstack.springframework.context.event;

import cn.bugstack.springframework.context.ApplicationEvent;
import cn.bugstack.springframework.context.ApplicationListener;

/**
 * 事件广播器
 */
public interface ApplicationEventMulticaster {
    /**
     * 添加监听
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * 删除监听
     */
    void removeApplicationListener(ApplicationListener<?> listener);

    /**
     * 广播事件
     */
    void multicastEvent(ApplicationEvent event);
}
