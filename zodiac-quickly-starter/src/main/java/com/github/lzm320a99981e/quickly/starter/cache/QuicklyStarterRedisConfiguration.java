package com.github.lzm320a99981e.quickly.starter.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@ConditionalOnMissingBean(RedisTemplate.class)
@EnableCaching
@Configuration
public class QuicklyStarterRedisConfiguration extends CachingConfigurerSupport {
    /**
     * redis操作模板
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
        final FastJsonConfig fastJsonConfig = new FastJsonConfig();
        serializer.setFastJsonConfig(fastJsonConfig);
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setSerializerFeatures(getSerializerFeatures());

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /**
     * 缓存管理器
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public CacheManager redisCacheManager(RedisTemplate<String, Object> redisTemplate) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        final RedisSerializationContext.SerializationPair keySerializer = RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getKeySerializer());
        final RedisSerializationContext.SerializationPair valueSerializer = RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer());

        RedisCacheConfiguration defaultRedisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(keySerializer)
                .serializeValuesWith(valueSerializer);

        return new RedisCacheManager(redisCacheWriter, defaultRedisCacheConfiguration);
    }

    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            if (params.length == 0) {
                return "";
            }
            if (params.length == 1) {
                return JSON.toJSONString(params[0], getSerializerFeatures());
            }
            return JSON.toJSONString(params, getSerializerFeatures());
        };
    }

    private SerializerFeature[] getSerializerFeatures() {
        return new SerializerFeature[]{SerializerFeature.SortField, SerializerFeature.MapSortField};
    }
}
