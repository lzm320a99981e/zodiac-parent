package com.github.lzm320a99981e.quickly.starter.storage;

import com.github.lzm320a99981e.quickly.starter.storage.dto.FileDownloadEntry;
import com.github.lzm320a99981e.zodiac.tools.ExceptionHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件上传拦截器
 */
public class DefaultFileDownloadInterceptor implements FileDownloadInterceptor {
    @Override
    public List<FileDownloadEntry> keysTransform(Collection<String> keys, File location) {
        try {
            return
                    Files.walk(location.toPath()).filter(
                            item -> {
                                final File file = item.toFile();
                                return file.isFile() && keys.contains(file.getName());
                            }
                    ).map(
                            item -> {
                                try {
                                    final FileDownloadEntry entry = new FileDownloadEntry();
                                    final File file = item.toFile();
                                    entry.setName(file.getName());
                                    entry.setData(Files.readAllBytes(item));
                                    entry.setPath(file.getAbsolutePath().substring(location.getAbsolutePath().length(), file.getAbsolutePath().length() - file.getName().length()));
                                    return entry;
                                } catch (IOException e) {
                                    throw ExceptionHelper.wrappedRuntimeException(e);
                                }
                            }
                    ).collect(Collectors.toList());
        } catch (IOException e) {
            throw ExceptionHelper.wrappedRuntimeException(e);
        }
    }
}
