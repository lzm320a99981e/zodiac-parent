package com.github.lzm320a99981e.component.validation;

import lombok.Getter;

/**
 * 自定义校验异常
 *
 * @since 1.0
 */
@Getter
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -2534100972637937003L;
    private String code;
    private String message;
    private Object[] arguments;

    public ValidationException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ValidationException(String code, String message, Object[] arguments) {
        this.code = code;
        this.message = message;
        this.arguments = arguments;
    }

    public ValidationException(Throwable cause, String code, String message) {
        super(cause);
        this.code = code;
        this.message = message;
    }

    public ValidationException(Throwable cause, String code, String message, Object[] arguments) {
        super(cause);
        this.code = code;
        this.message = message;
        this.arguments = arguments;
    }
}
