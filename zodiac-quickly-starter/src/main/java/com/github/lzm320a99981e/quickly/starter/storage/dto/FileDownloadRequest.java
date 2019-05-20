package com.github.lzm320a99981e.quickly.starter.storage.dto;

import java.util.List;

/**
 * 文件下载请求参数
 */
public class FileDownloadRequest {
    /**
     * 文件存储唯一标识
     */
    private List<String> saveKeys;

    /**
     * 下载名称（默认将使用上传时候保存的名称）
     */
    private String downloadName;
}
