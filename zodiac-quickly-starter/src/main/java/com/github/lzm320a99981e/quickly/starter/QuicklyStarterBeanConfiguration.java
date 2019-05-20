package com.github.lzm320a99981e.quickly.starter;

import com.github.lzm320a99981e.component.token.TokenManager;
import com.github.lzm320a99981e.component.validation.LocaleValidationExceptionHandler;
import com.github.lzm320a99981e.component.validation.ValidationExceptionHandler;
import com.github.lzm320a99981e.quickly.starter.api.ApiProperties;
import com.github.lzm320a99981e.quickly.starter.api.ApiResponse;
import com.github.lzm320a99981e.quickly.starter.storage.FileUploadInterceptor;
import com.github.lzm320a99981e.quickly.starter.storage.StorageManager;
import com.github.lzm320a99981e.quickly.starter.storage.StorageProperties;
import com.github.lzm320a99981e.quickly.starter.token.TokenProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * 自定义Bean配置
 */
@Configuration
public class QuicklyStarterBeanConfiguration {

    /**
     * Api 参数配置
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(Constants.ENV_PREFIX + "api")
    public ApiProperties apiProperties() {
        return new ApiProperties();
    }


    /**
     * 校验异常处理器
     *
     * @param messageSource
     * @return
     */
    @Bean(Constants.BEAN_NAME_PREFIX + "ValidationExceptionHandler")
    public ValidationExceptionHandler validationExceptionHandler(MessageSource messageSource, ApiProperties apiProperties) {
        ApiResponse apiResponse = apiProperties.getStatusCode().getInvalidRequestParameter();
        return new LocaleValidationExceptionHandler(messageSource, apiResponse.getCode(), apiResponse.getMessage());
    }

    /**
     * Token 参数配置
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(Constants.ENV_PREFIX + "token")
    public TokenProperties tokenProperties() {
        return new TokenProperties();
    }

    /**
     * Token 管理器
     *
     * @param tokenProperties
     * @return
     */
    @Bean
    public TokenManager tokenManager(TokenProperties tokenProperties) {
        final TokenManager tokenManager = new TokenManager(tokenProperties.getBase64EncodedSecretKey());
        if (Objects.nonNull(tokenProperties.getExpiresIn())) {
            tokenManager.setAccessTokenDurationInSeconds(tokenProperties.getExpiresIn());
        }
        if (Objects.nonNull(tokenProperties.getRefreshExpiresIn())) {
            tokenManager.setRefreshTokenDurationInSeconds(tokenProperties.getRefreshExpiresIn());
        }
        return tokenManager;
    }

    /**
     * 存储参数配置
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(Constants.ENV_PREFIX + "storage")
    public StorageProperties storageProperties() {
        return new StorageProperties();
    }

    /**
     * 存储管理器
     *
     * @param properties
     * @param interceptor
     * @return
     */
    @Bean
    public StorageManager storageManager(StorageProperties properties, @Autowired(required = false) FileUploadInterceptor interceptor) {
        return new StorageManager(properties, interceptor);
    }
}
