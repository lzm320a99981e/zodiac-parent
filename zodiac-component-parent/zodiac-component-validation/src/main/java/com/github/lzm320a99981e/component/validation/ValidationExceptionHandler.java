package com.github.lzm320a99981e.component.validation;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * 校验异常处理器
 *
 * <p>当发生校验异常的时候，会执行实现此接口({@link ValidationExceptionHandler})的异常处理器
 *
 * @since 1.0
 */
public interface ValidationExceptionHandler {
    /**
     * 方法参数可使用的校验注解，主要用于 {@link org.springframework.web.bind.annotation.RequestBody} 参数
     *
     * @param e
     * @return
     * @see javax.validation.Valid
     * @see org.springframework.web.bind.annotation.RequestBody
     */
    ValidationException handle(MethodArgumentNotValidException e);

    /**
     * 方法参数校验异常，针对表单参数
     *
     * @param e
     * @return
     * @see javax.validation.Valid
     */
    ValidationException handle(BindException e);

    /**
     * 自定义校验异常 用于业务校验不通过的时候抛出的异常
     *
     * @param e
     * @return
     */
    ValidationException handle(ValidationException e);
}
