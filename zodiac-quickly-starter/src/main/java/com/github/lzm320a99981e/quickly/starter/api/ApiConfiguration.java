package com.github.lzm320a99981e.quickly.starter.api;

import com.github.lzm320a99981e.quickly.starter.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(Constants.BEAN_NAME_PREFIX + "ApiConfiguration")
public class ApiConfiguration implements WebMvcConfigurer {

    @Autowired
    private ApiInternalInterceptor apiInternalInterceptor;

    @Autowired
    private ApiExternalInterceptor apiExternalInterceptor;

    @Autowired
    private ApiProperties apiProperties;

    /**
     * 拦截器配置
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiExternalInterceptor).addPathPatterns("/" + apiProperties.getRouter().getExternalPrefix() + "/**");
        registry.addInterceptor(apiInternalInterceptor).addPathPatterns("/" + apiProperties.getRouter().getInternalPrefix() + "/**");
    }
}
