package com.zodiac.app.gsps.endpoint.common;

import com.alibaba.fastjson.JSON;
import com.github.lzm320a99981e.quickly.starter.Constants;
import com.github.lzm320a99981e.quickly.starter.cache.CacheRedisHelper;
import com.zodiac.app.gsps.endpoint.common.dto.PersonalLoginRequest;
import com.zodiac.app.gsps.endpoint.common.dto.PersonalLoginResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("${" + Constants.ENV_PREFIX + "api.router.public-prefix:/public}")
public class PublicEndpoint {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("100101")
    public Object login(@Valid @RequestBody PersonalLoginRequest request) {
        return request;
    }

    @PostMapping("cache-test")
    public Object cacheTest(@Valid @RequestBody PersonalLoginRequest request) {
        redisTemplate.opsForValue().set("cache-test1", request);
        final Object result = redisTemplate.opsForValue().get("cache-test1");
        System.out.println(result);
        return request;
    }

    @Cacheable(cacheNames = "test01")
    @PostMapping("cache-test2")
    public Map test2(@RequestBody PersonalLoginRequest request) {
        return (Map) JSON.toJSON(request);
    }

    @PostMapping("cache-test3")
    public PersonalLoginResponse test3(@RequestBody PersonalLoginRequest request) {
        String key = "test03";
        final PersonalLoginResponse cacheValue = CacheRedisHelper.get(key, PersonalLoginResponse.class);
        if (Objects.nonNull(cacheValue)) {
            return cacheValue;
        }

        final PersonalLoginResponse response = new PersonalLoginResponse();
        BeanUtils.copyProperties(request, response);
        CacheRedisHelper.set("test03", request, Duration.ofMinutes(1));
        return response;
    }
}
