package com.github.lzm320a99981e.cloud.commons;

import com.github.lzm320a99981e.component.validation.ValidationExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 自定义控制层通知
 */
@Slf4j
@ControllerAdvice(basePackages = {"com.github.lzm320a99981e", "com.zodiac.app"})
public class CustomizeControllerAdvice {
    /**
     * 自定义控制层异常处理器
     */
    @Autowired(required = false)
    private CustomizeControllerExceptionHandler customizeControllerExceptionHandler;

    /**
     * 校验异常处理器
     */
    @Autowired
    private ValidationExceptionHandler validationExceptionHandler;

    /**
     * 控制层异常处理
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object handle(HttpServletRequest request, HttpServletResponse response, Exception e) {
        /**
         * 设置标识，此次请求已经过全局异常处理
         * @see CustomizeFilterInterceptorApi
         */
        request.setAttribute(CustomizeConstants.REQUEST_ATTRIBUTE_CUSTOMIZE_CONTROLLER_EXCEPTION_PROCESSED, e);
        if (Objects.isNull(this.customizeControllerExceptionHandler)) {
            this.customizeControllerExceptionHandler = new CustomizeDefaultControllerExceptionHandler(validationExceptionHandler);
        }
        return this.customizeControllerExceptionHandler.handle(request, response, e);
    }
}
