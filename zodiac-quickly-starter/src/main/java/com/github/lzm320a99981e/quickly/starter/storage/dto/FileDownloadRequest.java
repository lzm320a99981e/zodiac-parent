package com.github.lzm320a99981e.quickly.starter.storage.dto;

import com.github.lzm320a99981e.component.validation.Check;
import lombok.Data;

import java.util.List;

/**
 * 文件下载请求参数
 */
@Data
public class FileDownloadRequest {
    /**
     * 文件存储唯一标识
     */
    @Check
    private List<String> saveKeys;

    /**
     * 下载名称（默认将使用上传时候保存的名称）
     */
    private String downloadName;

    /**
     * 是否返回base64编码的数据
     */
    private boolean base64Encoded = false;
}
