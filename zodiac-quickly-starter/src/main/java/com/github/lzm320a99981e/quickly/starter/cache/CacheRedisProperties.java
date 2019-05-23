package com.github.lzm320a99981e.quickly.starter.cache;

import lombok.Data;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Data
public class CacheRedisProperties {
    /**
     * 缓存名称下的缓存项配置
     */
    private Map<String, CacheEntry> cacheNameWithCacheEntryMap;

    /**
     * 消息通道
     */
    private List<String> topics;

    @Data
    public static class CacheEntry {
        private Duration ttl;
        private boolean allowNullValues = true;
        private String keyPrefix;
    }
}
