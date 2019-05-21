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
        // 最外层异常处理
        ApiResponse result = handle(e);
        if (Objects.nonNull(result)) {
            return result;
        }

        // 如果最外层异常不能正确处理，则获取嵌套异常进行处理
        Throwable rootCause = NestedExceptionUtils.getRootCause(e);
        if (Objects.isNull(rootCause)) {
            return ApiResponse.error();
        }

        result = handle((Exception) rootCause);
        if (Objects.nonNull(result)) {
            return result;
        }

        // 未知异常
        return ApiResponse.error();
    }

    public ApiResponse handle(Exception e) {
        // 校验异常
        if (ValidationException.class.isAssignableFrom(e.getClass())) {
            return exceptionToApiResponse(validationExceptionHandler.handle((ValidationException) e));
        }

        // 接口业务异常
        if (ApiException.class.isAssignableFrom(e.getClass())) {
            return ((ApiException) e).getApiResponse();
        }

        // 请求参数异常（参数格式错误，导致参数不可读）
        if (HttpMessageNotReadableException.class.isAssignableFrom(e.getClass())) {
            return ApiResponse.invalidRequestBody();
        }

        // 请求体参数校验异常(@RequestBody标识的参数)
        if (MethodArgumentNotValidException.class.isAssignableFrom(e.getClass())) {
            return exceptionToApiResponse(validationExceptionHandler.handle((MethodArgumentNotValidException) e));
        }

        // 表单参数校验异常
        if (BindException.class.isAssignableFrom(e.getClass())) {
            return exceptionToApiResponse(validationExceptionHandler.handle((BindException) e));
        }
        return null;
    }

    private ApiResponse exceptionToApiResponse(ValidationException e) {
        return ApiResponse.create(e.getCode(), e.getMessage());
    }
}
