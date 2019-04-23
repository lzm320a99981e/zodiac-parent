package com.github.lzm320a99981e.component.weixinpay;

import lombok.Getter;

@Getter
public class WeixinPayException extends RuntimeException {
    /**
     * 错误码
     */
    private String code;
    /**
     * 错误消息
     */
    private String msg;
    /**
     * 错误类型
     */
    private Type type;


    public WeixinPayException(String code, String msg, Type type) {
        this.code = code;
        this.msg = msg;
        this.type = type;
    }

    public enum Type {
        /**
         * 请求通信异常
         */
        RETURN,
        /**
         * 接口业务异常
         */
        RESULT,
        /**
         * 签名错误
         */
        SIGN
    }
}
