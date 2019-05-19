package com.github.lzm320a99981e.quickly.starter.api;

import lombok.Getter;

/**
 * API异常信息
 */
@Getter
public class ApiException extends RuntimeException {
    private ApiResponse apiResponse;

    public ApiException(ApiResponse apiResponse) {
        this.apiResponse = apiResponse;
    }

    public ApiException(Throwable cause, ApiResponse apiResponse) {
        super(cause);
        this.apiResponse = apiResponse;
    }

    @Override
    public String getMessage() {
        return this.apiResponse.getMessage();
    }
}
