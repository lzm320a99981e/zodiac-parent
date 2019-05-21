package com.github.lzm320a99981e.quickly.starter.storage.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

@Data
public class FileUploadRequest {
    /**
     * 上传文件参数名称
     */
    private String parameterName;

    /**
     * 上传文件原始名称
     */
    private String originalFilename;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 文件内容类型
     */
    private String contentType;

    /**
     * 文件内容
     */
    @JSONField(serialize = false)
    private byte[] content;

    /**
     * 请求头信息
     */
    private Map<String, Object> headerMap;

    /**
     * 存储唯一标识
     */
    private String saveKey;

    /**
     * 存储相对路径
     */
    private String savePath;

    /**
     * 存储名称（默认将使用 originalFilename）
     */
    private String saveName;

    /**
     * 存储分类
     */
    private String saveClassification;

    /**
     * 是否覆盖
     */
    private boolean override;

    /**
     * 附加信息
     */
    private Map<String, Object> additionalMap;
}
