package com.github.lzm320a99981e.quickly.starter.storage.dto;

import lombok.Data;

/**
 * 文件上传响应信息
 */
@Data
public class FileUploadResponse {
    /**
     * 上传文件参数名称
     */
    private String parameterName;

    /**
     * 上传文件原始名称
     */
    private String originalFilename;

    /**
     * 文件存储标识
     */
    private String saveKey;
}
