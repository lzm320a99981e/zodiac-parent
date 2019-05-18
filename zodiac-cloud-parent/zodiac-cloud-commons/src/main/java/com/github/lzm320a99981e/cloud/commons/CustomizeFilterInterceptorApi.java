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
import java.util.Objects;

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

                // 异常返回值，不需要进行包装，直接返回
                if (Objects.nonNull(request.getAttribute(CustomizeConstants.REQUEST_ATTRIBUTE_CUSTOMIZE_CONTROLLER_EXCEPTION_PROCESSED))) {
                    writerAndFlush(responseWrapper, responseText);
                    return;
                }

                // 正常返回，需要把数据进行包装
                writerAndFlush(responseWrapper, JSON.toJSONString(ApiResponse.success(JSON.parse(responseText))));
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
            writerAndFlush(responseWrapper, JSON.toJSONString(ApiResponse.error()));
        }
    }

    private void writerAndFlush(ContentCachingResponseWrapper responseWrapper, String text) {
        try {
            responseWrapper.resetBuffer();
            responseWrapper.getWriter().write(text);
        } catch (IOException e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
    }

}
