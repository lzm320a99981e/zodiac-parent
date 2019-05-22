package com.github.lzm320a99981e.quickly.starter.storage;

import lombok.Data;

import java.io.File;
import java.util.Map;

/**
 * 文件存储配置信息
 */
@Data
public class StorageProperties {
    /**
     * 文件上传存储位置
     */
    private File location = new File("default-upload-folder");

    /**
     * 文件分类参数后缀(这个参数名需要添加上传文件参数名称的后面)
     */
    private String classificationParameterSuffix = "Classification";

    /**
     * 文件覆盖参数后缀(这个参数名需要添加上传文件参数名称的后面)
     */
    private String overrideKeyParameterSuffix = "OverrideKey";

    /**
     * 文件分类存储映射, key：类别，value：存储相对路径
     */
    private Map<String, String> classificationMap;

    /**
     * 默认配置为异常上传
     */
    private boolean async = true;

    /**
     * 客户端可配置的异步参数名称
     */
    private String asyncParameterName = "async";
}
