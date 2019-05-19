package com.github.lzm320a99981e.quickly.starter.api;

import com.github.lzm320a99981e.component.validation.ValidationException;
import com.github.lzm320a99981e.component.validation.ValidationExceptionHandler;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 默认控制层异常处理器
 */
public class CustomizeDefaultExceptionHandler implements CustomizeExceptionHandler {
    private final ValidationExceptionHandler validationExceptionHandler;

    public CustomizeDefaultExceptionHandler(ValidationExceptionHandler validationExceptionHandler) {
        this.validationExceptionHandler = validationExceptionHandler;
    }

    @Override
    public Object handle(HttpServletRequest request, HttpServletResponse response, Exception e) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(e);
        if (Objects.isNull(rootCause)) {
            rootCause = e;
        }
        final Class<? extends Throwable> causeClass = rootCause.getClass();

        // 请求参数异常（参数格式错误，导致参数不可读）
        if (HttpMessageNotReadableException.class.isAssignableFrom(causeClass)) {
            return ApiResponse.invalidRequestParameter();
        }

        // 业务校验异常
        if (ValidationException.class.isAssignableFrom(causeClass)) {
            return exceptionToApiResponse(validationExceptionHandler.handle((ValidationException) rootCause));
        }

        // 请求体参数校验异常(@RequestBody标识的参数)
        if (MethodArgumentNotValidException.class.isAssignableFrom(causeClass)) {
            return exceptionToApiResponse(validationExceptionHandler.handle((MethodArgumentNotValidException) rootCause));
        }

        // 表单参数校验异常
        if (BindException.class.isAssignableFrom(causeClass)) {
            return exceptionToApiResponse(validationExceptionHandler.handle((BindException) rootCause));
        }

        // 未知异常
        return ApiResponse.error();
    }

    private ApiResponse exceptionToApiResponse(ValidationException e) {
        return ApiResponse.create(e.getCode(), e.getMessage());
    }
}
