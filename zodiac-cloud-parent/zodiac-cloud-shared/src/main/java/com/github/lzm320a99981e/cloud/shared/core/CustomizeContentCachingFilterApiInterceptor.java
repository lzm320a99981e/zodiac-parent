package com.github.lzm320a99981e.cloud.shared.core;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;

@Data
public class CustomizeContentCachingFilterApiInterceptor implements CustomizeContentCachingFilterInterceptor {
    private Collection<String> urlPatterns = Arrays.asList("/100", "/api/100");

    @Override
    public void before(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getParameterMap());
    }
}
