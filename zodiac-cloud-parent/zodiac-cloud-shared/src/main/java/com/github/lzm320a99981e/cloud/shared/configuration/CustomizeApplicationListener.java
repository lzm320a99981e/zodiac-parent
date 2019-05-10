package com.github.lzm320a99981e.cloud.shared.configuration;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 自定义-监听器
 */
@Component
public class CustomizeApplicationListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        if (event instanceof ApplicationReadyEvent) {

            onReady((ApplicationReadyEvent) event);
            return;
        }
        if (event instanceof ApplicationFailedEvent) {

            onFailed((ApplicationFailedEvent) event);
        }
    }

    /**
     * 初始化完成
     */
    private void onReady(ApplicationReadyEvent event) {
//        redisTemplate.convertAndSend("topic:channel:test", "哈哈");
//        redisTemplate.opsForValue().set("key1", "value1", 5, TimeUnit.SECONDS);

//        MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor()).submit(
//                () -> {
//                }
//        );
    }

    /**
     * 启动失败
     *
     * @param event
     */
    private void onFailed(ApplicationFailedEvent event) {
//        MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor()).submit(
//                () -> System.out.println("初始化")
//        );
    }
}
