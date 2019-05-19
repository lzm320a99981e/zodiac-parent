package com.zodiac.app.gsps.endpoint;


import com.github.lzm320a99981e.component.validation.ValidationException;
import com.github.lzm320a99981e.quickly.starter.ErrorCodeHelper;

/**
 * 错误码
 */
public enum ErrorCode {
    AUTH_TOKEN_4001,
    AUTH_TOKEN_4002,
    ;
    private String code;

    ErrorCode() {
        this.code = ErrorCodeHelper.parseErrorCode(this);
    }

    public void throwException(Object... arguments) {
        throw getException(arguments);
    }

    public ValidationException getException(Object... arguments) {
        return new ValidationException(this.code, this.name(), arguments);
    }
}
