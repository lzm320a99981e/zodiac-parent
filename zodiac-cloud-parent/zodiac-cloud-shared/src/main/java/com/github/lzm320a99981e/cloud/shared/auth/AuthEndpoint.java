package com.github.lzm320a99981e.cloud.shared.auth;

import com.alibaba.fastjson.JSONObject;
import com.github.lzm320a99981e.cloud.shared.core.ApiException;
import com.github.lzm320a99981e.cloud.shared.core.ApiResponse;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class AuthEndpoint {

    @PostMapping("100")
    public Object test1(@RequestBody JSONObject params) {
        final HashMap<Object, Object> response = Maps.newHashMap();
        response.put("name", "张三");
        if (!params.containsKey("aa")) {
            throw new ApiException(ApiResponse.error());
        }
        return response;
    }

    @PostMapping("api/100")
    public Object test3(@RequestBody JSONObject params) {
        final HashMap<Object, Object> response = Maps.newHashMap();
        response.put("name", "张三");
        if (!params.containsKey("aa")) {
            throw new ApiException(ApiResponse.error());
        }
        return response;
    }

    @PostMapping("200")
    public Object test2(@RequestBody JSONObject params) {
        final HashMap<Object, Object> response = Maps.newHashMap();
        response.put("name", "张三");
        if (!params.containsKey("aa")) {
            throw new ApiException(ApiResponse.error());
        }
        return response;
    }

}
