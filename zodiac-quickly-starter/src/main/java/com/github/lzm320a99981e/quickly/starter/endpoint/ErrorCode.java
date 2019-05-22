package com.github.lzm320a99981e.quickly.starter.endpoint;


import com.github.lzm320a99981e.quickly.starter.ErrorCodeHelper;
import com.github.lzm320a99981e.quickly.starter.api.ApiException;
import com.github.lzm320a99981e.quickly.starter.api.ApiResponse;

import java.util.Objects;

/**
 * 错误码
 */
public enum ErrorCode {
    REFRESH_TOKEN_4001("无效的刷新令牌"),
    REFRESH_TOKEN_4002("过期的刷新令牌"),
    FILE_UPLOAD_4001("未获取到上传的文件"),
    FILE_UPLOAD_4002("上传文件内容为空，参数名称：[{0}]，文件名称：[{1}]"),
    FILE_UPLOAD_4003("分类信息不存在，分类参数名称：[{0}]，分类参数值：[{1}]"),
    FILE_UPLOAD_4004("覆盖文件不存在，覆盖参数名称：[{0}]，覆盖参数值：[{1}]"),
    FILE_DOWNLOAD_4001("下载的文件不存在：[{0}]"),
    ;
    private String code;
    private String message;

    ErrorCode(String message) {
        this.code = ErrorCodeHelper.parseErrorCode(this);
        this.message = message;
    }

    public void throwException(String... arguments) {
        throw getException(arguments);
    }

    public ApiException getException(String... arguments) {
        String finalMessage = String.valueOf(this.message);
        if (Objects.nonNull(arguments)) {
            for (int i = 0; i < arguments.length; i++) {
                finalMessage = finalMessage.replace("{" + i + "}", arguments[i]);
            }
        }
        return new ApiException(ApiResponse.create(this.code, finalMessage));
    }
}
