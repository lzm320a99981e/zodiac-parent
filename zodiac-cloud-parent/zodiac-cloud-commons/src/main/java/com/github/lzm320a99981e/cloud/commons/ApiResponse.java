package com.github.lzm320a99981e.cloud.commons;

import com.alibaba.fastjson.JSON;
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

    /**
     * 成功响应信息
     */
    private static final ApiResponse SUCCESS = ApiResponse.create("0", "OK");

    /**
     * 系统错误响应信息
     */
    private static final ApiResponse ERROR = ApiResponse.create("-1", "ERROR");

    public ApiResponse() {
    }

    private ApiResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ApiResponse create(String code, String message) {
        return new ApiResponse(code, message);
    }

    public static ApiResponse success() {
        return ApiResponse.create(SUCCESS.getCode(), SUCCESS.getMessage());
    }

    public static ApiResponse success(Object data) {
        return ApiResponse.create(SUCCESS.getCode(), SUCCESS.getMessage()).data(data);
    }

    public static ApiResponse error() {
        return ApiResponse.create(ERROR.getCode(), ERROR.getMessage());
    }

    public static ApiResponse invalidRequestParameter() {
        return ApiResponse.create("5001", "invalid request parameter");
    }

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
        return Objects.equals(SUCCESS.getCode(), getCode());
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
