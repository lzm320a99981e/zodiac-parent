package com.github.lzm320a99981e.quickly.starter.api;

import com.github.lzm320a99981e.quickly.starter.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * API 请求转发
 */
@Controller
@RequestMapping("${" + Constants.ENV_PREFIX + "api.router.external-prefix:/api}")
public class ApiForwardController {
    private final ApiProperties properties;

    public ApiForwardController(ApiProperties properties) {
        this.properties = properties;
    }

    @RequestMapping(value = "{serviceId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String forward(@PathVariable String serviceId, HttpServletRequest request) {
        // 标记从网关经过的请求
        request.setAttribute(Constants.REQUEST_ATTRIBUTE_MARK_API_FORWARDED, true);
        return "forward:/" + properties.getRouter().getInternalPrefix() + "/" + serviceId;
    }
}
