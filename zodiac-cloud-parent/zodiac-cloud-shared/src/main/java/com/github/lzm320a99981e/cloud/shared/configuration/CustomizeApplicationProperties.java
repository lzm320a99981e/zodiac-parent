
package com.github.lzm320a99981e.cloud.shared.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自定义-应用属性
 */
@Data
@ConfigurationProperties("customize")
public class CustomizeApplicationProperties {
    private Token token;

    @Data
    public static class Token {
        private String base64EncodedSecretKey;
        private Integer expiresIn;
        private Integer refreshExpiresIn;
    }
}