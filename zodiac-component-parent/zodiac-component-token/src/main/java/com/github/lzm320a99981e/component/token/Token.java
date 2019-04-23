package com.github.lzm320a99981e.component.token;

import lombok.Data;

/**
 * 令牌数据
 */
@Data
public class Token {
    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌，当访问令牌过期的时候，可用此令牌去生成一个新的访问令牌（此令牌的有效时间一般会大雨访问令牌的有效时间）
     */
    private String refreshToken;
    /**
     * 访问令牌有效时间（单位：秒）
     */
    private Integer expiresIn;
}
