package com.github.lzm320a99981e.cloud.shared.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

@Configuration
public class CustomizeBeanConfiguration {

    @Bean
    @ConfigurationProperties("customize.content-caching-filter-bean")
    public CustomizeContentCachingFilterBeanProperties customizeContentCachingFilterBeanProperties() {
        return new CustomizeContentCachingFilterBeanProperties();
    }

    @Bean
    public FilterRegistrationBean customizeContentCachingFilterBean(@Autowired(required = false) List<CustomizeContentCachingFilterInterceptor> interceptors, CustomizeContentCachingFilterBeanProperties properties) {
        final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setEnabled(properties.isEnabled());
        if (Objects.nonNull(properties.getName())) {
            filterRegistrationBean.setName(properties.getName());
        }
        if (Objects.nonNull(properties.getOrder())) {
            filterRegistrationBean.setOrder(properties.getOrder());
        }
        if (Objects.nonNull(properties.getUrlPatterns())) {
            filterRegistrationBean.setUrlPatterns(properties.getUrlPatterns());
        }
        if (Objects.nonNull(properties.getInitParameters())) {
            filterRegistrationBean.setInitParameters(properties.getInitParameters());
        }
        // 过滤器
        filterRegistrationBean.setFilter(new OncePerRequestFilter() {
            final PathMatcher pathMatcher = new AntPathMatcher();

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
                final ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
                final ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
                final String uri = request.getRequestURI();
                final boolean hasInterceptors = Objects.nonNull(interceptors) && !interceptors.isEmpty();
                try {
                    // 过滤器处理
                    if (hasInterceptors) {
                        interceptors.stream().filter(item -> item.getUrlPatterns().stream().anyMatch(urlPattern -> pathMatcher.match(urlPattern, uri))).forEach(item -> item.before(requestWrapper, responseWrapper));
                    }
                    filterChain.doFilter(requestWrapper, responseWrapper);
                    // 过滤后处理
                    if (hasInterceptors) {
                        interceptors.forEach(item -> item.after(requestWrapper, responseWrapper));
                    }
                } catch (Exception e) {
                    // 发生异常时处理
                    if (hasInterceptors) {
                        interceptors.forEach(item -> item.onException(requestWrapper, responseWrapper, e));
                    }
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
        filterRegistrationBean.setEnabled(true);
        return filterRegistrationBean;
    }

}
