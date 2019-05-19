package com.github.lzm320a99981e.quickly.starter.api;

import com.github.lzm320a99981e.component.token.TokenManager;
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
public class ApiInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private ApiProperties properties;
    @Autowired
    private TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tokenParameterName = properties.getTokenParameterName();
        // 校验 token
        String token = request.getHeader(tokenParameterName);
        if (Objects.isNull(token)) {
            token = request.getParameter(tokenParameterName);
        }
        if (Objects.isNull(token)) {
            throw new ApiException(ApiConstants.ERROR_RESPONSE_INVALID_ACCESS_TOKEN);
        }

        // 获取主体信息
//        Principal principal = tokenAuthService.getPrincipal(accessToken);

        // 保存 token 关联的用户信息到 request attribute
//        request.setAttribute(ApiConstants.REQUEST_ATTRIBUTE_TOKEN_BODY, principal);
        return true;
    }
}
