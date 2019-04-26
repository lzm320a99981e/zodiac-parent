package com.github.lzm320a99981e.cloud.shared.core;

import lombok.Data;

import java.util.Collection;
import java.util.Map;

@Data
public class CustomizeContentCachingFilterBeanProperties {
    private boolean enabled;
    private String name;
    private int order;
    private Collection<String> urlPatterns;
    private Map<String, String> initParameters;
}
