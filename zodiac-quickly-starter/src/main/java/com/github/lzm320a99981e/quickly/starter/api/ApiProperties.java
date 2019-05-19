package com.github.lzm320a99981e.quickly.starter.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * API 配置项
 */
@Data
@Configuration
@ConfigurationProperties("quickly.starter.api")
public class ApiProperties {
    /**
     * 是否允许直接访问内部接口
     */
    private boolean internalAccessEnabled = false;

    /**
     * 令牌参数名称
     */
    private String tokenParameterName = "token";

    /**
     * 路由
     */
    private Router router = new Router();

    /**
     * 公共状态码
     */
    private StatusCode statusCode = new StatusCode();

    /**
     * 路由配置
     */
    @Data
    public static class Router {
        /**
         * 公共请求路由前缀
         */
        private String publicPrefix = "public";
        /**
         * 内部请求路由前缀
         */
        private String internalPrefix = "internal";
        /**
         * 外部请求路由前缀
         */
        private String externalPrefix = "api";
    }

    /**
     * 公共状态码配置
     */
    @Data
    public static class StatusCode {
        private ApiResponse success = ApiResponse.create("0", "OK");
        private ApiResponse error = ApiResponse.create("-1", "Error");
        private ApiResponse invalidRequestParameter = ApiResponse.create("5000", "无效的请求参数[{}]");
        private ApiResponse invalidRequestBody = ApiResponse.create("5001", "请求参数格式错误");
        private ApiResponse invalidRequestUrl = ApiResponse.create("5002", "无效的请求路径");
    }
}
