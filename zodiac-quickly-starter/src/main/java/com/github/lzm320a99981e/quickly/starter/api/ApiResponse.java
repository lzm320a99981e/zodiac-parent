package com.github.lzm320a99981e.quickly.starter.api;

import com.alibaba.fastjson.JSON;
import com.github.lzm320a99981e.quickly.starter.SpringContextHelper;
import com.google.common.base.Preconditions;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * API 响应信息
 */
@Data
public class ApiResponse {
    /**
     * 编码
     */
    private String code;
    /**
     * 信息
     */
    private String message;
    /**
     * 数据
     */
    private Object data;

    public ApiResponse() {
    }

    private ApiResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取配置的状态码
     *
     * @return
     */
    private static ApiProperties.StatusCode statusCode() {
        return SpringContextHelper.getBean(ApiProperties.class).getStatusCode();
    }

    /**
     * 创建响应对象
     *
     * @param code
     * @param message
     * @return
     */
    public static ApiResponse create(String code, String message) {
        return new ApiResponse(code, message);
    }

    /**
     * 成功
     *
     * @return
     */
    public static ApiResponse success() {
        return success(null);
    }

    /**
     * 成功
     *
     * @param data
     * @return
     */
    public static ApiResponse success(Object data) {
        ApiResponse configured = statusCode().getSuccess();
        return ApiResponse.create(configured.getCode(), configured.getMessage()).data(data);
    }

    /**
     * 系统异常
     *
     * @return
     */
    public static ApiResponse error() {
        ApiResponse configured = statusCode().getError();
        return ApiResponse.create(configured.getCode(), configured.getMessage());
    }

    /**
     * 拒绝访问
     *
     * @return
     */
    public static ApiResponse forbidden() {
        ApiResponse configured = statusCode().getForbidden();
        return ApiResponse.create(configured.getCode(), configured.getMessage());
    }

    /**
     * 无效的请求参数
     *
     * @return
     */
    public static ApiResponse invalidRequestParameter() {
        ApiResponse configured = statusCode().getInvalidRequestParameter();
        return ApiResponse.create(configured.getCode(), configured.getMessage());
    }

    /**
     * 无效的访问路径
     *
     * @return
     */
    public static ApiResponse invalidRequestUrl() {
        ApiResponse configured = statusCode().getInvalidRequestUrl();
        return ApiResponse.create(configured.getCode(), configured.getMessage());
    }

    /**
     * 请求参数格式错误
     *
     * @return
     */
    public static ApiResponse invalidRequestBody() {
        ApiResponse configured = statusCode().getInvalidRequestBody();
        return ApiResponse.create(configured.getCode(), configured.getMessage());
    }

    /**
     * 无效的访问令牌
     *
     * @return
     */
    public static ApiResponse invalidToken() {
        ApiResponse configured = statusCode().getInvalidToken();
        return ApiResponse.create(configured.getCode(), configured.getMessage());
    }

    /**
     * 过期的访问令牌
     *
     * @return
     */
    public static ApiResponse expiredToken() {
        ApiResponse configured = statusCode().getExpiredToken();
        return ApiResponse.create(configured.getCode(), configured.getMessage());
    }

    /**
     * 响应数据
     *
     * @param data
     * @return
     */
    public ApiResponse data(Object data) {
        this.data = data;
        return this;
    }


    /**
     * 解析数据作为一个对象
     *
     * @param type
     * @param <T>
     * @return
     */
    public <T> T parseDataAsObject(Class<T> type) {
        assertSuccessful();
        return JSON.parseObject(JSON.toJSONString(Preconditions.checkNotNull(this.data)), type);
    }

    /**
     * 解析数据作为一个数组
     *
     * @param type
     * @param <T>
     * @return
     */
    public <T> List<T> parseDataAsArray(Class<T> type) {
        assertSuccessful();
        return JSON.parseArray(JSON.toJSONString(Preconditions.checkNotNull(this.data)), type);
    }

    /**
     * 判断请求是否成功
     *
     * @return
     */
    public boolean successful() {
        return Objects.equals(success().getCode(), getCode());
    }

    /**
     * 断言返回成功
     */
    public void assertSuccessful() {
        if (!successful()) {
            throw new ApiException(this);
        }
    }
}
