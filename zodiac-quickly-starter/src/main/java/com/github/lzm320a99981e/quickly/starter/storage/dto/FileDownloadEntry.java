package com.github.lzm320a99981e.quickly.starter.storage.dto;

import lombok.Data;

/**
 * 文件下载条目
 */
@Data
public class FileDownloadEntry {
    /**
     * 名称
     */
    private String name;

    /**
     * 数据
     */
    private byte[] data;

    /**
     * 路径(在多文件打包下载的时候可能会用到)
     */
    private String path;
}
