
package com.github.lzm320a99981e.cloud.shared.configuration;

import com.github.lzm320a99981e.component.token.TokenManager;
import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * 自定义-应用配置
 */
@Data
@Configuration
@EnableConfigurationProperties(CustomizeApplicationProperties.class)
public class CustomizeApplicationConfiguration {
    private final CustomizeApplicationProperties properties;

    public CustomizeApplicationConfiguration(CustomizeApplicationProperties properties) {
        this.properties = properties;
    }

    /**
     * 令牌管理器
     *
     * @return
     */
    @Bean
    public TokenManager tokenManager() {
        final CustomizeApplicationProperties.Token token = properties.getToken();
        final TokenManager tokenManager = new TokenManager(token.getBase64EncodedSecretKey());
        if (Objects.nonNull(token.getExpiresIn())) {
            tokenManager.setAccessTokenDurationInSeconds(token.getExpiresIn());
        }
        if (Objects.nonNull(token.getRefreshExpiresIn())) {
            tokenManager.setRefreshTokenDurationInSeconds(token.getRefreshExpiresIn());
        }
        return tokenManager;
    }
}