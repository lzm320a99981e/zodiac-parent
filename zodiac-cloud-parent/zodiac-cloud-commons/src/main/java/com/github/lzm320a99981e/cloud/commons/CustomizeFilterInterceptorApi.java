package com.github.lzm320a99981e.cloud.commons;

import com.alibaba.fastjson.JSON;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * API 拦截器
 */
@Component
@ConfigurationProperties("customize.filter-interceptor-api")
public class CustomizeFilterInterceptorApi extends CustomizeAbstractFilterInterceptor {
    /**
     * 请求后处理
     *
     * @param request
     * @param response
     */
    @Override
    public void after(HttpServletRequest request, HttpServletResponse response) {
        if (ContentCachingResponseWrapper.class.isAssignableFrom(response.getClass())) {
            ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
            try {
                final String responseText = StreamUtils.copyToString(responseWrapper.getContentInputStream(), Charset.forName(responseWrapper.getCharacterEncoding()));
                final ApiResponse apiResponse = ApiResponse.success(JSON.parse(responseText));
                responseWrapper.resetBuffer();
                responseWrapper.getWriter().write(JSON.toJSONString(apiResponse));
            } catch (IOException e) {
                ReflectionUtils.rethrowRuntimeException(e);
            }
        }
    }

    /**
     * 发生异常时处理
     *
     * @param request
     * @param response
     * @param ex
     */
    @Override
    public void onException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        if (ContentCachingResponseWrapper.class.isAssignableFrom(response.getClass())) {
            ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
            try {
                responseWrapper.resetBuffer();
                responseWrapper.getWriter().write(JSON.toJSONString(ApiResponse.error()));
            } catch (IOException e) {
                ReflectionUtils.rethrowRuntimeException(e);
            }
        }
    }

}
