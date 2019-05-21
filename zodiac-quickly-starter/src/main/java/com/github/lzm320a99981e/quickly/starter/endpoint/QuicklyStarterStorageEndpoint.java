package com.github.lzm320a99981e.quickly.starter.endpoint;

import com.github.lzm320a99981e.quickly.starter.Constants;
import com.github.lzm320a99981e.quickly.starter.storage.StorageManager;
import com.github.lzm320a99981e.quickly.starter.storage.dto.FileDownloadRequest;
import com.github.lzm320a99981e.quickly.starter.storage.dto.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;

/**
 * 存储接口
 */
@RestController
@RequestMapping("${" + Constants.ENV_PREFIX + "api.router.internal-prefix:/public}")
public class QuicklyStarterStorageEndpoint {
    @Autowired
    private StorageManager storageManager;

    /**
     * 文件上传
     *
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping(value = ServiceId.STORAGE_FILE_UPLOAD, consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ResponseBody
    public Object upload(MultipartHttpServletRequest request) throws IOException {
        final List<FileUploadResponse> responses = storageManager.upload(request);
        if (responses.size() == 1) {
            return responses.get(0);
        }
        return responses;
    }

    /**
     * 文件下载
     *
     * @param request
     */
    @RequestMapping(value = ServiceId.STORAGE_FILE_DOWNLOAD, method = {RequestMethod.POST, RequestMethod.GET})
    public void download(FileDownloadRequest request) {
        storageManager.download(request);
    }
}
