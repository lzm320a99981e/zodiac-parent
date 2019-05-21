package com.github.lzm320a99981e.quickly.starter.storage;

import com.alibaba.fastjson.JSON;
import com.github.lzm320a99981e.quickly.starter.RequestContextHelper;
import com.github.lzm320a99981e.quickly.starter.endpoint.ErrorCode;
import com.github.lzm320a99981e.quickly.starter.storage.dto.FileUploadRequest;
import com.github.lzm320a99981e.quickly.starter.storage.dto.FileUploadResponse;
import com.github.lzm320a99981e.zodiac.tools.ExceptionHelper;
import com.github.lzm320a99981e.zodiac.tools.IdGenerator;
import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 存储服务
 */
@Slf4j
public class StorageManager {
    private final StorageProperties properties;
    private final FileUploadInterceptor interceptor;

    public StorageManager(StorageProperties properties, FileUploadInterceptor interceptor) {
        this.properties = properties;
        this.interceptor = Objects.isNull(interceptor) ? new DefaultFileUploadInterceptor() : interceptor;
    }

    /**
     * 文件上传
     *
     * @param request
     * @return
     * @throws IOException
     */
    public List<FileUploadResponse> upload(MultipartHttpServletRequest request) throws IOException {
        final MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();

        // 判断是否存在上传的文件
        if (multiFileMap.isEmpty()) {
            ErrorCode.FILE_UPLOAD_4001.throwException();
        }

        // 遍历上传文件列表
        final List<FileUploadRequest> fileUploadRequests = new ArrayList<>();
        for (Map.Entry<String, List<MultipartFile>> entry : multiFileMap.entrySet()) {
            final List<MultipartFile> files = entry.getValue();
            for (MultipartFile file : files) {
                // 判断上传文件是否为空
                if (file.isEmpty()) {
                    ErrorCode.FILE_UPLOAD_4002.throwException(file.getName(), file.getOriginalFilename());
                }
                // 文件上传拦截处理
                final FileUploadRequest fileUploadRequest = createFileUploadRequest(file, request);
                if (interceptor.preHandle(fileUploadRequest)) {
                    fileUploadRequests.add(fileUploadRequest);
                }
            }
        }

        // 打印上传文件信息
        log.info("\n++++++++++++++++++++++++++ 上传文件信息 +++++++++++++++++++++++++++\n{}", JSON.toJSONString(fileUploadRequests, true));

        // 异步处理上传的文件
        handleUpload(fileUploadRequests);

        // 返回
        return fileUploadRequests.stream().map(item -> {
            final FileUploadResponse fileUploadResponse = new FileUploadResponse();
            fileUploadResponse.setParameterName(item.getParameterName());
            fileUploadResponse.setOriginalFilename(item.getOriginalFilename());
            fileUploadResponse.setSaveKey(item.getSaveKey());
            return fileUploadResponse;
        }).collect(Collectors.toList());
    }

    /**
     * 创建上传文件信息
     *
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    private FileUploadRequest createFileUploadRequest(MultipartFile file, MultipartHttpServletRequest request) throws IOException {
        final Map<String, String> classificationMap = properties.getClassificationMap();
        final String classificationParameterSuffix = properties.getClassificationParameterSuffix();

        final FileUploadRequest upload = new FileUploadRequest();
        final String parameterName = file.getName();
        upload.setParameterName(parameterName);
        upload.setOriginalFilename(file.getOriginalFilename());
        upload.setSize(file.getSize());
        upload.setContentType(file.getContentType());
        upload.setContent(file.getBytes());
        upload.setHeaderMap(RequestContextHelper.getHeaderMap());
        upload.setSaveKey(IdGenerator.uuid32());

        final String classificationParameterName = parameterName + classificationParameterSuffix;
        final String classification = request.getParameter(classificationParameterName);
        if (Objects.nonNull(classification)) {
            if (Objects.isNull(classificationMap) || !classificationMap.containsKey(classification)) {
                // 未找到匹配的分类信息
                ErrorCode.FILE_UPLOAD_4003.throwException(classificationParameterName, classification);
            }
            upload.setSaveType(classification);
            upload.setSavePath(classificationMap.get(classification));
        }

        return upload;
    }

    /**
     * 文件上传处理
     *
     * @param fileUploadRequests
     */
    private void handleUpload(List<FileUploadRequest> fileUploadRequests) {
        for (FileUploadRequest request : fileUploadRequests) {
            final ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
            final ListenableFuture<FileUploadRequest> future = service.submit(() -> doUpload(request));
            Futures.addCallback(
                    future,
                    new FutureCallback<FileUploadRequest>() {
                        @Override
                        public void onSuccess(@Nullable FileUploadRequest result) {
                            interceptor.onSuccess(result);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            interceptor.onFailure(request, t);
                        }
                    },
                    MoreExecutors.directExecutor()
            );
        }
    }

    /**
     * 写入磁盘
     *
     * @param request
     */
    private FileUploadRequest doUpload(FileUploadRequest request) {
        final File location = properties.getLocation();
        final String savePath = request.getSavePath();
        final String saveKey = request.getSaveKey();
        try {
            final Path path = Paths.get(location.getAbsolutePath(), Objects.isNull(savePath) ? "" : savePath, saveKey);
            if (!path.getParent().toFile().exists()) {
                path.getParent().toFile().mkdirs();
            }
            log.info("\n++++++++++++++++++++++++++ 文件上传-开始 +++++++++++++++++++++++++++\n{} -> {}", request.getSaveKey(), path.toFile().getAbsolutePath());
            final long startTime = System.currentTimeMillis();
            Files.write(path, request.getContent());
            final long usedTime = System.currentTimeMillis() - startTime;
            log.info("\n++++++++++++++++++++++++++ 文件上传-结束(用时: {} 毫秒) +++++++++++++++++++++++++++\n{} -> {}", usedTime, request.getSaveKey(), path.toFile().getAbsolutePath());
            return request;
        } catch (IOException e) {
            throw ExceptionHelper.wrappedRuntimeException(e);
        }
    }
}
