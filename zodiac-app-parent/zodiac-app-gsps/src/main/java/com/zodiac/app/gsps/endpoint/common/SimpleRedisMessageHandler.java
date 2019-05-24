package com.zodiac.app.gsps.endpoint.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.lzm320a99981e.quickly.starter.cache.RedisMessageHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
@Component
public class SimpleRedisMessageHandler implements RedisMessageHandler {
    @Override
    public void handleMessage(RedisTemplate redisTemplate, Message message, byte[] pattern) {
        final JSONObject result = new JSONObject();
        result.put("pattern", new String(pattern, StandardCharsets.UTF_8));
        result.put("channel", new String(message.getChannel(), StandardCharsets.UTF_8));
        result.put("body", redisTemplate.getKeySerializer().deserialize(message.getBody()));
        System.out.println(JSON.toJSONString(result, true));
    }
}
