package com.github.lzm320a99981e.cloud.shared.core;

import lombok.Data;

import java.util.Collection;
import java.util.Map;

/**
 * 自定义内容缓存过滤器Bean配置属性
 */
@Data
public class CustomizeContentCachingFilterBeanProperties {
    private boolean enabled = true;
    private String name;
    private Integer order;
    private Collection<String> urlPatterns;
    private Map<String, String> initParameters;
}
