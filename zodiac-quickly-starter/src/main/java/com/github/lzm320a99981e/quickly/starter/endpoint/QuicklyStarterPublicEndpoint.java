package com.github.lzm320a99981e.quickly.starter.endpoint;

import com.github.lzm320a99981e.component.token.Token;
import com.github.lzm320a99981e.component.token.TokenException;
import com.github.lzm320a99981e.component.token.TokenManager;
import com.github.lzm320a99981e.quickly.starter.Constants;
import com.github.lzm320a99981e.quickly.starter.api.ApiException;
import com.github.lzm320a99981e.quickly.starter.api.ApiResponse;
import com.github.lzm320a99981e.quickly.starter.endpoint.dto.RefreshTokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("${" + Constants.ENV_PREFIX + "api.router.public-prefix:/public}")
public class QuicklyStarterPublicEndpoint {
    @Autowired
    private TokenManager tokenManager;

    @PostMapping("refresh-token")
    public Token refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            return tokenManager.refresh(request.getRefreshToken());
        } catch (TokenException e) {
            if (TokenException.Type.EXPIRED == e.getType()) {
                throw new ApiException(ApiResponse.create("4001", "过期的刷新令牌"));
            }
            throw new ApiException(ApiResponse.create("4002", "无效的刷新令牌"));
        }
    }
}
