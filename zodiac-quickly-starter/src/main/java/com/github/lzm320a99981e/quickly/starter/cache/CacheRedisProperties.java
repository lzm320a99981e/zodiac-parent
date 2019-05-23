package com.github.lzm320a99981e.quickly.starter.cache;

import lombok.Data;

import java.time.Duration;
import java.util.Map;

@Data
public class CacheRedisProperties {
    private Map<String, CacheEntry> cacheEntryMap;

    @Data
    public static class CacheEntry {
        private Duration ttl;
        private boolean allowNullValues = true;
        private String keyPrefix;
    }
}
