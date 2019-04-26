package com.github.lzm320a99981e.cloud.shared.core;

import lombok.Data;

import java.util.Collection;

@Data
public abstract class CustomizeAbstractFilterInterceptor implements CustomizeFilterInterceptor {
    private boolean enabled;
    private Collection<String> urlPatterns;
}
