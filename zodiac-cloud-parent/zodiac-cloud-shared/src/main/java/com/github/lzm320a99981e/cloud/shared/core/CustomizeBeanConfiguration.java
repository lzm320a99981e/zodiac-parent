package com.github.lzm320a99981e.cloud.shared.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

@Configuration
public class CustomizeBeanConfiguration {
    @Bean
    public FilterRegistrationBean apiLoggingFilterRegistration() {
        final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                final ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
                final ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
                try {
                    filterChain.doFilter(requestWrapper, responseWrapper);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    final String responseText = StreamUtils.copyToString(responseWrapper.getContentInputStream(), Charset.forName(responseWrapper.getCharacterEncoding()));
                    final JSONObject data = JSON.parseObject(responseText);
                    final JSONObject responseBody = new JSONObject();
                    responseBody.put("code", "2000");
                    responseBody.put("message", "OK");
                    responseBody.put("data", data);
                    responseWrapper.resetBuffer();
                    responseWrapper.getWriter().write(JSON.toJSONString(responseBody));
                    responseWrapper.copyBodyToResponse();
                }
            }
        });

        return filterRegistrationBean;
    }

}
