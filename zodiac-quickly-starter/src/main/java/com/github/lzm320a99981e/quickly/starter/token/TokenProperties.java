package com.github.lzm320a99981e.quickly.starter.token;

import lombok.Data;

@Data
public class TokenProperties {
    private String base64EncodedSecretKey;
    private Integer expiresIn;
    private Integer refreshExpiresIn;
}
