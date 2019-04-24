package com.github.lzm320a99981e.component.validation;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.metadata.ConstraintDescriptor;
import java.util.Map;
import java.util.Objects;

/**
 * 可以返回国际化校验信息的校验异常处理器
 *
 * @since 1.0
 */
public class LocaleValidationExceptionHandler implements ValidationExceptionHandler {
    /**
     * 错误信息中，错误参数占位符
     */
    private static final String ERROR_MESSAGE_PARAMETER_PLACEHOLDER = "{}";
    /**
     * 校验字段参数名
     */
    private static final String VALIDATION_FIELD_PARAMETER_NAME = "parameter";
    /**
     * 国际化消息资源
     */
    private final MessageSource messageSource;
    /**
     * 错误码
     */
    private final String errorCode;
    /**
     * 错误信息
     */
    private final String errorMessage;


    public LocaleValidationExceptionHandler(MessageSource messageSource, String errorCode, String errorMessage) {
        this.messageSource = messageSource;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public ValidationException handle(MethodArgumentNotValidException e) {
        return handle(e.getBindingResult());
    }

    @Override
    public ValidationException handle(BindException e) {
        return handle(e.getBindingResult());
    }

    private ValidationException handle(BindingResult bindingResult) {
        final ConstraintViolationImpl violation = bindingResult.getAllErrors().get(0).unwrap(ConstraintViolationImpl.class);
        final ConstraintDescriptor constraintDescriptor = violation.getConstraintDescriptor();
        // @Check 自定义公共校验注解
        if (Objects.equals(Check.class, constraintDescriptor.getAnnotation().annotationType())) {
            final String messageTemplate = constraintDescriptor.getMessageTemplate();
            final Map attributes = constraintDescriptor.getAttributes();

            // 校验的参数名称
            String parameter = (String) attributes.get(VALIDATION_FIELD_PARAMETER_NAME);
            if (Objects.isNull(parameter) || parameter.isEmpty()) {
                parameter = violation.getPropertyPath().toString();
            }

            // 不存在消息模板
            if (Objects.isNull(messageTemplate) || messageTemplate.isEmpty()) {
                return new ValidationException(errorCode, errorMessage.replace(ERROR_MESSAGE_PARAMETER_PLACEHOLDER, parameter));
            }

            // 存在消息模板（从消息资源文件里面获取）
            final String message = messageSource.getMessage(messageTemplate, new Object[]{parameter}, LocaleContextHolder.getLocale());
            return new ValidationException(errorCode, message);
        }
        // 暂不支持其他校验注解
        return new ValidationException(errorCode, errorMessage.replace(ERROR_MESSAGE_PARAMETER_PLACEHOLDER, ""));
    }

    @Override
    public ValidationException handle(ValidationException e) {
        final String message = messageSource.getMessage(e.getMessage(), e.getArguments(), LocaleContextHolder.getLocale());
        return new ValidationException(e.getCode(), message);
    }
}
