package com.github.lzm320a99981e.quickly.starter.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * API 请求转发
 */
@Controller
@RequestMapping("${quickly.starter.api.router.external-prefix:/api}")
public class ApiForwardController {
    @Autowired
    private ApiProperties properties;

    @RequestMapping(value = "{serviceId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String forward(@PathVariable String serviceId, HttpServletRequest request) {
        // 检查 service id 是否存在
        if (!ServiceIdExtractor.existsServiceId(serviceId)) {
            throw new ApiException(ApiConstants.ERROR_RESPONSE_INVALID_SERVICE_ID);
        }
        // 保存 service id 到 request attribute
        request.setAttribute(ApiConstants.REQUEST_ATTRIBUTE_SERVICE_ID, serviceId);

        // 标记从网关经过的请求
        request.setAttribute(ApiConstants.REQUEST_ATTRIBUTE_MARK_API_FORWARDED, true);
        return "forward:/" + properties.getRoute().getInnerPrefix() + "/" + serviceId;
    }
}
