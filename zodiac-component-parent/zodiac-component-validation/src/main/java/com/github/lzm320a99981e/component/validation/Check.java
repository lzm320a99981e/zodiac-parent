package com.github.lzm320a99981e.component.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 校验注解，提供多种形式的校验
 */
@Documented
@Constraint(validatedBy = {CheckValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Check {
    /**
     * 参数名称，默认为字段名称
     *
     * @return
     */
    String parameter() default "";

    /**
     * 正则表达式
     *
     * @return
     */
    String regex() default "";

    /**
     * 日期格式
     * 例如：
     * yyyy-MM-dd
     * yyyy-MM-dd HH:mm:ss
     * yyyyMMdd
     * yyyyMMddHHmmss
     *
     * @return
     */
    String datePattern() default "";

    /**
     * 数字区间
     * 例如：
     * open -> (3,5)
     * openClosed -> (3,5]
     * closedOpen -> [3,5)
     * closed -> [3,5]
     *
     * @return
     */
    String numberRange() default "";

    /**
     * 长度区间
     * 例如：
     * open -> (3,5)
     * openClosed -> (3,5]
     * closedOpen -> [3,5)
     * closed -> [3,5]
     *
     * @return
     */
    String lengthRange() default "";

    /**
     * 处理器
     *
     * @return
     */
    Class<? extends CheckHandler>[] handlers() default {};

    /**
     * 错误信息模板
     *
     * @return
     */
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
