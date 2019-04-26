package com.github.lzm320a99981e.cloud.shared.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class CustomizeGlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResponse handle(HttpServletRequest request, Exception e) {
        log.error("微服务 :: 公共组件 :: 全局异常处理 :: " + request.getRequestURI(), e);
        Throwable rootCause = NestedExceptionUtils.getRootCause(e);
        if (Objects.isNull(rootCause)) {
            rootCause = e;
        }
        final Class<? extends Throwable> causeClass = rootCause.getClass();
        if (ApiException.class.isAssignableFrom(causeClass)) {
            return ((ApiException) rootCause).getApiResponse();
        }
        return ApiResponse.error();
    }
}
