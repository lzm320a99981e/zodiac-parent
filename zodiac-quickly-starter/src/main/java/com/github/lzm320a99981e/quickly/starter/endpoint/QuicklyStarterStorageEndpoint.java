package com.github.lzm320a99981e.quickly.starter.endpoint;

import com.github.lzm320a99981e.quickly.starter.Constants;
import com.github.lzm320a99981e.quickly.starter.storage.StorageManager;
import com.github.lzm320a99981e.quickly.starter.storage.dto.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;

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

}
