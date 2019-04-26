package com.github.lzm320a99981e.cloud.shared.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;

/**
 * 自定义内容缓存过滤器处理器（实现此接口即可获取请求对象输入流和响应对象输出流数据）
 *
 * @see org.springframework.web.util.ContentCachingResponseWrapper
 * @see org.springframework.web.util.ContentCachingResponseWrapper
 */
public interface CustomizeFilterInterceptor {
    /**
     * 是否启动拦截器
     *
     * @return {@code true} 启动 ${@code false} 禁用
     */
    default boolean isEnabled() {
        return true;
    }

    /**
     * 拦截路径匹配
     *
     * @return
     */
    default Collection<String> getUrlPatterns() {
        return Collections.singletonList("/*");
    }

    /**
     * 过滤前拦截
     *
     * @param request
     * @param response
     */
    default void before(HttpServletRequest request, HttpServletResponse response) {
    }

    /**
     * 过滤后拦截
     *
     * @param request
     * @param response
     */
    default void after(HttpServletRequest request, HttpServletResponse response) {
    }

    /**
     * 发生异常时拦截
     *
     * @param request
     * @param response
     * @param ex
     */
    default void onException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
    }
}
