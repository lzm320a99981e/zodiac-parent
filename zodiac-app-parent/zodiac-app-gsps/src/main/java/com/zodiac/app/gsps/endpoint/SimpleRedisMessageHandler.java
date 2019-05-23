package com.zodiac.app.gsps.endpoint;

import com.github.lzm320a99981e.quickly.starter.cache.RedisMessageHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.StandardCharsets;

//@Component
public class SimpleRedisMessageHandler implements RedisMessageHandler {
    @Override
    public void handleMessage(RedisTemplate redisTemplate, Message message, byte[] pattern) {
        System.out.println(new String(message.getChannel(), StandardCharsets.UTF_8));
    }
}
