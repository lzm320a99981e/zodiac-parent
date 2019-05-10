package com.github.lzm320a99981e.cloud.shared.endpoint.auth;

import com.github.lzm320a99981e.cloud.shared.endpoint.ErrorCode;
import com.github.lzm320a99981e.cloud.shared.endpoint.ServiceId;
import com.github.lzm320a99981e.cloud.shared.endpoint.auth.dto.RefreshTokenRequest;
import com.github.lzm320a99981e.cloud.shared.endpoint.auth.dto.VerifyTokenRequest;
import com.github.lzm320a99981e.component.token.Token;
import com.github.lzm320a99981e.component.token.TokenException;
import com.github.lzm320a99981e.component.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 认证接口
 */
@RestController
public class AuthEndpoint {

    @Autowired
    private TokenManager tokenManager;

    /**
     * 颁发令牌
     *
     * @param payload
     * @return
     */
    @PostMapping(ServiceId.AUTH_TOKEN_ISSUE)
    public Token issueToken(@RequestBody Map<String, Object> payload) {
        if (!payload.isEmpty()) {
            return tokenManager.generate();
        }
        return tokenManager.generate(payload);
    }

    /**
     * 刷新令牌
     *
     * @param request
     * @return
     */
    @PostMapping(ServiceId.AUTH_TOKEN_REFRESH)
    public Token refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            return tokenManager.refresh(request.getRefreshToken());
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    /**
     * 校验令牌，校验通过
     *
     * @param request
     * @return
     */
    @PostMapping(ServiceId.AUTH_TOKEN_VERIFY)
    public Map<String, Object> verifyToken(@RequestBody VerifyTokenRequest request) {
        try {
            return tokenManager.verify(request.getToken());
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    /**
     * 令牌异常处理
     *
     * @param e
     */
    private void handleException(Exception e) {
        if (e instanceof TokenException) {
            final TokenException.Type type = ((TokenException) e).getType();
            if (type == TokenException.Type.EXPIRED) {
                ErrorCode.AUTU_TOKEN_4002.throwException();
            }
            ErrorCode.AUTU_TOKEN_4001.throwException();
        }
        ErrorCode.AUTU_TOKEN_4001.throwException();
    }

}
