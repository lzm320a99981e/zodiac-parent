package com.github.lzm320a99981e.component.validation;

import com.github.lzm320a99981e.zodiac.tools.DateUtils;
import com.github.lzm320a99981e.zodiac.tools.ExceptionHelper;
import com.github.lzm320a99981e.zodiac.tools.ext.spring.SpringContextUtils;
import com.google.common.collect.Range;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.core.annotation.AnnotationUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 通用校验注解实现
 */
@Slf4j
public class CheckValidator implements ConstraintValidator<Check, Object> {
    private Check parameters;

    @Override
    public void initialize(Check parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            // 添加参数到上下文，方便后续取出来处理
            Map<String, Object> attributes = AnnotationUtils.getAnnotationAttributes(parameters);
            HibernateConstraintValidatorContext validatorContext = context.unwrap(HibernateConstraintValidatorContext.class);
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                validatorContext.addMessageParameter(entry.getKey(), entry.getValue());
            }

            // 优先使用自定义处理器
            final Class<? extends CheckHandler>[] handlers = parameters.handlers();
            if (handlers.length > 0) {
                for (Class<? extends CheckHandler> handler : handlers) {
                    CheckHandler checkHandler = SpringContextUtils.getBean(handler);
                    if (!checkHandler.handle(parameters, value, context)) {
                        return false;
                    }
                }
            }

            // 非空判断
            if (Objects.isNull(value)) {
                return false;
            }

            // 集合
            if (Collection.class.isAssignableFrom(value.getClass())) {
                return !((Collection) value).isEmpty();
            }
            if (Map.class.isAssignableFrom(value.getClass())) {
                return !((Map) value).isEmpty();
            }

            // 数组
            if (value.getClass().isArray()) {
                return Array.getLength(value) > 0;
            }

            // 字符串
            if (CharSequence.class.isAssignableFrom(value.getClass())) {
                String chars = value.toString();
                // 非空字符串校验
                if (chars.trim().length() == 0) {
                    return false;
                }

                // 正则表达式校验
                final String regex = parameters.regex();
                if (!regex.isEmpty() && !chars.matches(regex)) {
                    return false;
                }

                // 日期格式校验
                String datePattern = parameters.datePattern();
                if (!datePattern.isEmpty()) {
                    try {
                        DateUtils.parse(value.toString(), datePattern);
                    } catch (Exception e) {
                        return false;
                    }
                }

                // 数字区间校验
                final String numberRange = parameters.numberRange();
                if (!numberRange.isEmpty() && !parseRange(numberRange).check(Double.valueOf(chars))) {
                    return false;
                }

                // 长度区间校验
                final String lengthRange = parameters.lengthRange();
                if (!lengthRange.isEmpty() && !parseRange(lengthRange).check(chars.length())) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.error("校验发生异常 -> 校验值 : " + value, e);
            return false;
        }
    }

    private RangeBean parseRange(String range) {
        try {
            range = range.replace(" ", "");
            final RangeBean rangeBean = new RangeBean();
            // 边界
            rangeBean.setLimit(range.substring(0, 1) + range.substring(range.length() - 1));
            // 范围
            String[] lowerAndUpper = range.substring(1, range.length() - 1).split(",");
            rangeBean.setLower(Double.valueOf(lowerAndUpper[0]));
            rangeBean.setUpper(Double.valueOf(lowerAndUpper[1]));
            return rangeBean;
        } catch (Exception e) {
            throw ExceptionHelper.wrappedRuntimeException(e);
        }
    }

    @Data
    private static class RangeBean {
        private String limit;
        private Double lower;
        private Double upper;

        /**
         * 区间校验
         *
         * @param number
         * @return
         */
        boolean check(Number number) {
            final double num = number.doubleValue();
            switch (limit) {
                case "()":
                    return Range.open(lower, upper).contains(num);
                case "(]":
                    return Range.openClosed(lower, upper).contains(num);
                case "[)":
                    return Range.closedOpen(lower, upper).contains(num);
                case "[]":
                    return Range.closed(lower, upper).contains(num);
                default:
                    throw new RuntimeException("不支持的区间：" + limit);
            }
        }
    }
}

