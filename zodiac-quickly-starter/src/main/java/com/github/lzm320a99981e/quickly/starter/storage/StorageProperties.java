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
    private File location;

    /**
     * 文件分类参数后缀(这个参数名需要添加上传文件参数名称的后面)
     */
    private String classificationParameterSuffix;

    /**
     * 文件分类存储映射, key：类别，value：存储相对路径
     */
    private Map<String, String> classificationMap;
}
