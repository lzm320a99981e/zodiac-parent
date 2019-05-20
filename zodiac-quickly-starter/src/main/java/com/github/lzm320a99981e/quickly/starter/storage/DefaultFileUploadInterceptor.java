package com.github.lzm320a99981e.quickly.starter.storage;

import com.alibaba.fastjson.JSON;
import com.github.lzm320a99981e.quickly.starter.storage.dto.FileUploadRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认上传拦截器实现（打印日志）
 */
@Slf4j
public class DefaultFileUploadInterceptor implements FileUploadInterceptor {
    @Override
    public void onSuccess(FileUploadRequest request) {
        log.info("\n+++++++++++++++++++++++ 文件上传出成功 +++++++++++++++++++++++\n{}", JSON.toJSONString(request, true));
    }

    @Override
    public void onFailure(FileUploadRequest request, Throwable throwable) {
        log.error("\n+++++++++++++++++++++++ 文件上传出失败 +++++++++++++++++++++++\n" + JSON.toJSONString(request, true) + "\n{}", throwable);
    }
}
