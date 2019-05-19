package com.github.lzm320a99981e.quickly.starter;

import com.github.lzm320a99981e.component.validation.LocaleValidationExceptionHandler;
import com.github.lzm320a99981e.component.validation.ValidationExceptionHandler;
import com.github.lzm320a99981e.quickly.starter.api.ApiProperties;
import com.github.lzm320a99981e.quickly.starter.api.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义Bean配置
 */
@Configuration(Constants.BEAN_NAME_PREFIX + "GlobalBeanConfiguration")
public class GlobalBeanConfiguration {
    @Autowired
    private ApiProperties apiProperties;

    /**
     * 校验异常处理器
     *
     * @param messageSource
     * @return
     */
    @Bean(Constants.BEAN_NAME_PREFIX + "ValidationExceptionHandler")
    public ValidationExceptionHandler validationExceptionHandler(MessageSource messageSource) {
        ApiResponse apiResponse = apiProperties.getStatusCode().getInvalidRequestParameter();
        return new LocaleValidationExceptionHandler(messageSource, apiResponse.getCode(), apiResponse.getMessage());
    }
}
