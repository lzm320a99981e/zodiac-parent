package com.github.lzm320a99981e.quickly.starter.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 缓存辅助类
 */
public class CacheRedisHelper {
    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * 初始化
     *
     * @param redisTemplate
     */
    static void initialize(RedisTemplate<String, Object> redisTemplate) {
        CacheRedisHelper.redisTemplate = redisTemplate;
    }

    /**
     * 获取缓存值
     *
     * @param key
     * @return
     */
    public static Object get(Object key) {
        return redisTemplate.opsForValue().get(toCacheKey(key));
    }

    /**
     * 获取缓存值
     *
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T get(Object key, Class<T> type) {
        final Object cacheValue = get(key);
        if (Objects.isNull(cacheValue)) {
            return null;
        }
        if (CharSequence.class.isAssignableFrom(cacheValue.getClass())) {
            return JSON.parseObject(cacheValue.toString(), type);
        }
        return JSON.parseObject(JSON.toJSONString(cacheValue), type);
    }

    /**
     * 获取缓存值
     *
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> getArray(Object key, Class<T> type) {
        final Object cacheValue = get(key);
        if (Objects.isNull(cacheValue)) {
            return null;
        }
        if (CharSequence.class.isAssignableFrom(cacheValue.getClass())) {
            return JSON.parseArray(cacheValue.toString(), type);
        }
        return JSON.parseArray(JSON.toJSONString(cacheValue), type);
    }

    /**
     * 存在覆盖，不存在添加
     *
     * @param key
     * @param value
     * @param timeout
     */
    public static void set(Object key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(toCacheKey(key), value, timeout);
    }

    /**
     * 存在覆盖，不存在添加
     *
     * @param key
     * @param value
     */
    public static void set(Object key, Object value) {
        redisTemplate.opsForValue().set(toCacheKey(key), value);
    }

    /**
     * 存在则添加
     *
     * @param key
     * @param value
     * @param timeout
     */
    public static void setIfPresent(Object key, Object value, Duration timeout) {
        redisTemplate.opsForValue().setIfPresent(toCacheKey(key), value, timeout);
    }

    /**
     * 存在则添加
     *
     * @param key
     * @param value
     */
    public static void setIfPresent(Object key, Object value) {
        redisTemplate.opsForValue().setIfPresent(toCacheKey(key), value);
    }

    /**
     * 不存在则添加
     *
     * @param key
     * @param value
     * @param timeout
     */
    public static void setIfAbsent(Object key, Object value, Duration timeout) {
        redisTemplate.opsForValue().setIfAbsent(toCacheKey(key), value, timeout);
    }

    /**
     * 不存在则添加
     *
     * @param key
     * @param value
     */
    public static void setIfAbsent(Object key, Object value) {
        redisTemplate.opsForValue().setIfAbsent(toCacheKey(key), value);
    }

    /**
     * 匹配Key
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 判断是否存在Key
     *
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据Key删除缓存
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 根据Key删除缓存
     *
     * @param keys
     * @return
     */
    public long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 获取模板
     *
     * @return
     */
    public static RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 转换为缓存Key
     *
     * @param key
     * @return
     */
    private static String toCacheKey(Object key) {
        if (CharSequence.class.isAssignableFrom(key.getClass())) {
            return key.toString();
        }
        return JSON.toJSONString(key, getSerializerFeatures());
    }

    private static SerializerFeature[] getSerializerFeatures() {
        return new SerializerFeature[]{SerializerFeature.SortField, SerializerFeature.MapSortField};
    }
}
