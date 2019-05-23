package com.github.lzm320a99981e.quickly.starter.cache;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis 消息处理器
 */
public interface RedisMessageHandler {
    /**
     * 处理消息
     *
     * @param message
     * @param pattern
     */
    void handleMessage(RedisTemplate redisTemplate, Message message, byte[] pattern);
}
