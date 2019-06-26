package com.github.lzm320a99981e.quickly.starter.storage;

import com.github.lzm320a99981e.quickly.starter.storage.dto.FileDownloadEntry;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * 文件下载拦截器
 */
public interface FileDownloadInterceptor {
    /**
     * 文件存储Key转换为可下载的文件条目
     *
     * @param keys     文件存储唯一标识
     * @param location 文件存储位置
     * @return
     */
    List<FileDownloadEntry> keysTransform(Collection<String> keys, File location);
}
