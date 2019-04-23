package com.github.lzm320a99981e.component.token;

import lombok.Getter;

/**
 * 令牌异常信息
 */
public class TokenException extends RuntimeException {
    @Getter
    private Type type;

    /**
     * 错误类型
     */
    public enum Type {
        /**
         * 无效
         */
        INVALID,
        /**
         * 已过期
         */
        EXPIRED
    }

    public TokenException(Throwable cause, Type type) {
        super(cause);
        this.type = type;
    }

    public TokenException(String message, Type type) {
        super(message);
        this.type = type;
    }
}
