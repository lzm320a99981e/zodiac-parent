package com.github.lzm320a99981e.cloud.commons;

import lombok.Data;

import java.util.Collection;

/**
 * 抽象过滤器拦截器
 */
@Data
public abstract class CustomizeAbstractFilterInterceptor implements CustomizeFilterInterceptor {
    private boolean enabled;
    private Collection<String> urlPatterns;
}
