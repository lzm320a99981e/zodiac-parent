package com.zodiac.app.gsps.endpoint.common;

import com.github.lzm320a99981e.quickly.starter.Constants;
import com.zodiac.app.gsps.endpoint.common.dto.PersonalLoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${" + Constants.ENV_PREFIX + "api.router.public-prefix:/public}")
public class PublicEndpoint {

    @PostMapping("100101")
    public Object login(@RequestBody PersonalLoginRequest request) {
        return request;
    }
}
