package com.github.lzm320a99981e.quickly.starter.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * API 内部请求（不支持直接请求内部接口，必须通过{@link ApiForwardController}转发到内部请求）
 */
@Component
public class ApiInnerInterceptor extends HandlerInterceptorAdapter {
    private final ApiProperties apiProperties;

    @Autowired
    public ApiInnerInterceptor(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 是否允许直接访问内部服务
        if (apiProperties.isInternalAccessEnabled()) {
            return true;
        }

        // 如果不是由 ApiForwardController 转发过来的请求，则禁止访问
        Object forwarded = request.getAttribute(ApiConstants.REQUEST_ATTRIBUTE_MARK_API_FORWARDED);
        if (Objects.isNull(forwarded)) {
            throw new ApiException(ApiConstants.ERROR_RESPONSE_FORBIDDEN);
        }
        return true;
    }
}
