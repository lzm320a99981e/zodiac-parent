package com.github.lzm320a99981e.quickly.starter.api;

import com.github.lzm320a99981e.quickly.starter.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(Constants.BEAN_NAME_PREFIX + "ApiConfiguration")
public class ApiConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }
}
