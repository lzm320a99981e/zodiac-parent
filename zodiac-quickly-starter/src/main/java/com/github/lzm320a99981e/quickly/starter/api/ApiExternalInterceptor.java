package com.github.lzm320a99981e.quickly.starter.api;

import com.github.lzm320a99981e.component.token.TokenException;
import com.github.lzm320a99981e.component.token.TokenManager;
import com.github.lzm320a99981e.quickly.starter.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * API 请求拦截（前置校验，相当于一个简单的网关层）
 */
@Component
public class ApiExternalInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private ApiProperties apiProperties;
    @Autowired
    private TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tokenParameterName = apiProperties.getTokenParameterName();
        // 校验 token
        String token = request.getHeader(tokenParameterName);
        if (Objects.isNull(token)) {
            token = request.getParameter(tokenParameterName);
        }
        if (Objects.isNull(token)) {
            throw new ApiException(apiProperties.getStatusCode().getInvalidToken());
        }

        try {
            // 保存 token 关联的用户信息到 request attribute
            request.setAttribute(Constants.REQUEST_ATTRIBUTE_TOKEN_BODY, tokenManager.verify(token));
        } catch (TokenException e) {
            if (TokenException.Type.EXPIRED == e.getType()) {
                throw new ApiException(apiProperties.getStatusCode().getExpiredToken());
            }
            throw new ApiException(apiProperties.getStatusCode().getInvalidToken());
        }

        return true;
    }
}
