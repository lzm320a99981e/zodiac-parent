package com.github.lzm320a99981e.quickly.starter.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.github.lzm320a99981e.quickly.starter.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ConditionalOnMissingBean(RedisTemplate.class)
@EnableCaching
@Configuration
public class CacheRedisConfiguration extends CachingConfigurerSupport {
    @Bean
    @ConfigurationProperties(Constants.ENV_PREFIX + "cache.redis")
    public CacheRedisProperties cacheRedisProperties() {
        return new CacheRedisProperties();
    }

    //=================================== 缓存配置 ====================================//

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
        // 设置到辅助类
        CacheRedisHelper.initialize(redisTemplate);
        return redisTemplate;
    }

    /**
     * 缓存管理器
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public CacheManager redisCacheManager(RedisTemplate redisTemplate, CacheRedisProperties properties) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        final RedisSerializationContext.SerializationPair keySerializer = RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getKeySerializer());
        final RedisSerializationContext.SerializationPair valueSerializer = RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer());

        RedisCacheConfiguration defaultRedisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(keySerializer)
                .serializeValuesWith(valueSerializer);
        // 自定义缓存配置
        final Map<String, CacheRedisProperties.CacheEntry> cacheNameWithCacheEntryMap = properties.getCacheNameWithCacheEntryMap();

        // 不存在自定义缓存配置
        if (Objects.isNull(cacheNameWithCacheEntryMap) || cacheNameWithCacheEntryMap.isEmpty()) {
            return new RedisCacheManager(redisCacheWriter, defaultRedisCacheConfiguration);
        }

        // 存在自定义缓存配置
        final Map<String, RedisCacheConfiguration> customizeRedisConfigurationMap = new HashMap<>();
        for (Map.Entry<String, CacheRedisProperties.CacheEntry> entry : cacheNameWithCacheEntryMap.entrySet()) {
            final CacheRedisProperties.CacheEntry cacheEntry = entry.getValue();
            final String cacheName = entry.getKey();
            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().serializeKeysWith(keySerializer).serializeValuesWith(valueSerializer);
            // 是否允许Null值
            if (!cacheEntry.isAllowNullValues()) {
                config = config.disableCachingNullValues();
            }
            // 过期时间
            if (Objects.nonNull(cacheEntry.getTtl())) {
                config = config.entryTtl(cacheEntry.getTtl());
            }
            // Key前缀
            if (Objects.nonNull(cacheEntry.getKeyPrefix())) {
                config = config.prefixKeysWith(cacheEntry.getKeyPrefix());
            }
            customizeRedisConfigurationMap.put(cacheName, config);
        }
        return new RedisCacheManager(redisCacheWriter, defaultRedisCacheConfiguration, customizeRedisConfigurationMap);
    }

    /**
     * 缓存Key生成器
     *
     * @return
     */
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

    //=================================== 消息监听 ====================================//

    /**
     * 消息监听器
     *
     * @return
     */
    @ConditionalOnBean(RedisMessageHandler.class)
    @Bean
    public MessageListener messageListener(RedisTemplate redisTemplate, RedisMessageHandler messageHandler) {
        return (message, pattern) -> messageHandler.handleMessage(redisTemplate, message, pattern);
    }

    /**
     * 消息监听容器
     *
     * @param redisConnectionFactory
     * @param properties
     * @param messageListener
     * @return
     */
    @ConditionalOnBean(RedisMessageHandler.class)
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory, CacheRedisProperties properties, MessageListener messageListener) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        List<String> topics = properties.getTopics();
        if (Objects.nonNull(topics) && !topics.isEmpty()) {
            container.addMessageListener(messageListener, topics.stream().map(ChannelTopic::new).collect(Collectors.toList()));
        }
        return container;
    }

}
