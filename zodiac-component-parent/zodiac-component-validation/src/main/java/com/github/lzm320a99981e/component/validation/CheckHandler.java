package com.github.lzm320a99981e.component.validation;

import javax.validation.ConstraintValidatorContext;

/**
 * 校验处理器接口，可以自定义校验处理器，实现此接口即可
 *
 * @since 1.0
 */
public interface CheckHandler {
    /**
     * 校验逻辑
     *
     * @param parameters 校验元注解
     * @param value      校验值
     * @param context    校验上下文
     * @return true:校验通过，false:校验不通过
     */
    boolean handle(Check parameters, Object value, ConstraintValidatorContext context);
}
