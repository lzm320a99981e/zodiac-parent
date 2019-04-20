package com.github.lzm320a99981e.compnent.token;

import lombok.Data;

import java.util.Map;

@Data
public class Token {
    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
    private Map<String, Object> body;
}
