package com.github.lzm320a99981e.quickly.starter.storage;

import com.github.lzm320a99981e.quickly.starter.storage.dto.FileUploadRequest;

/**
 * 文件上传拦截器
 */
public interface FileUploadInterceptor {
    /**
     * 上传前处理
     *
     * @param request
     * @return
     */
    default boolean preHandle(FileUploadRequest request) {
        return true;
    }

    /**
     * 上传成功
     *
     * @param request
     */
    default void onSuccess(FileUploadRequest request) {
    }

    /**
     * 上传失败
     *
     * @param request
     * @param throwable
     */
    default void onFailure(FileUploadRequest request, Throwable throwable) {
    }

}
