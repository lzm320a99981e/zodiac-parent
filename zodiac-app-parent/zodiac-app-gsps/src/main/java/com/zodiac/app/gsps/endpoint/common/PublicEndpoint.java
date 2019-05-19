package com.zodiac.app.gsps.endpoint.common;

import com.github.lzm320a99981e.quickly.starter.Constants;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("${" + Constants.ENV_PREFIX + "api.router.public-prefix:/public}")
public class PublicEndpoint {

    @PostMapping("100101")
    public Object login(@RequestBody Map<String, String> request) {
        return request;
    }
}
