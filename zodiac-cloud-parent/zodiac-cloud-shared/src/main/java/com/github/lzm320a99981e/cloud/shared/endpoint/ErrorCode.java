package com.github.lzm320a99981e.cloud.shared.endpoint;


import com.github.lzm320a99981e.cloud.commons.util.ErrorCodeUtils;
import com.github.lzm320a99981e.component.validation.ValidationException;

/**
 * 错误码
 */
public enum ErrorCode {
    AUTU_TOKEN_4001,
    AUTU_TOKEN_4002,
    ;
    private String code;

    ErrorCode() {
        this.code = ErrorCodeUtils.parseErrorCode(this);
    }

    public void throwException(Object... arguments) {
        throw getException(arguments);
    }

    public ValidationException getException(Object... arguments) {
        return new ValidationException(this.code, this.name(), arguments);
    }
}
