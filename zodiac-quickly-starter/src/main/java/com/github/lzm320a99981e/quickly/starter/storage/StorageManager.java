package com.github.lzm320a99981e.quickly.starter.storage;

import com.alibaba.fastjson.JSON;
import com.github.lzm320a99981e.quickly.starter.RequestContextHelper;
import com.github.lzm320a99981e.quickly.starter.endpoint.ErrorCode;
import com.github.lzm320a99981e.quickly.starter.storage.dto.FileDownloadEntry;
import com.github.lzm320a99981e.quickly.starter.storage.dto.FileDownloadRequest;
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
    private final FileUploadInterceptor uploadInterceptor;
    private final FileDownloadInterceptor downloadInterceptor;

    public StorageManager(StorageProperties properties, FileUploadInterceptor uploadInterceptor, FileDownloadInterceptor downloadInterceptor) {
        this.properties = properties;
        this.uploadInterceptor = Objects.isNull(uploadInterceptor) ? new DefaultFileUploadInterceptor() : uploadInterceptor;
        this.downloadInterceptor = Objects.isNull(downloadInterceptor) ? new DefaultFileDownloadInterceptor() : downloadInterceptor;
    }
    // ============================================ 文件上传 ============================================

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
                if (uploadInterceptor.preHandle(fileUploadRequest)) {
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
        final FileUploadRequest upload = new FileUploadRequest();
        final String parameterName = file.getName();
        upload.setParameterName(parameterName);
        upload.setOriginalFilename(file.getOriginalFilename());
        upload.setSize(file.getSize());
        upload.setContentType(file.getContentType());
        upload.setContent(file.getBytes());
        upload.setHeaderMap(RequestContextHelper.getHeaderMap());
        upload.setSaveKey(IdGenerator.uuid32());

        // 分类信息校验
        final Map<String, String> classificationMap = properties.getClassificationMap();
        final String classificationParameterSuffix = properties.getClassificationParameterSuffix();
        final String classificationParameterName = parameterName + classificationParameterSuffix;
        final String classification = request.getParameter(classificationParameterName);
        if (Objects.nonNull(classification)) {
            if (Objects.isNull(classificationMap) || !classificationMap.containsKey(classification)) {
                // 未找到匹配的分类信息
                ErrorCode.FILE_UPLOAD_4003.throwException(classificationParameterName, classification);
            }
            upload.setSaveClassification(classification);
            upload.setSavePath(classificationMap.get(classification));
        }

        // 覆盖信息校验
        final File location = properties.getLocation();
        final String overrideKeyParameterSuffix = properties.getOverrideKeyParameterSuffix();
        final String overrideKeyParameterName = parameterName + overrideKeyParameterSuffix;
        final String overrideKey = request.getParameter(overrideKeyParameterName);
        if (Objects.nonNull(overrideKey)) {
            final String savePath = upload.getSavePath();
            final Path overridePath = Paths.get(location.getAbsolutePath(), Objects.isNull(savePath) ? "" : savePath, overrideKey);
            if (!overridePath.toFile().exists()) {
                ErrorCode.FILE_UPLOAD_4004.throwException(overrideKeyParameterName, overrideKey);
            }
            upload.setOverride(true);
            upload.setSaveKey(overrideKey);
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
                            uploadInterceptor.onSuccess(result);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            uploadInterceptor.onFailure(request, t);
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
            log.info("\n++++++++++++++++++++++++++ 文件上传(" + (request.isOverride() ? "覆盖" : "新增") + ")-开始 +++++++++++++++++++++++++++\n{} -> {}", saveKey, path.toFile().getAbsolutePath());
            final long startTime = System.currentTimeMillis();
            Files.write(path, request.getContent());
            final long usedTime = System.currentTimeMillis() - startTime;
            log.info("\n++++++++++++++++++++++++++ 文件上传(" + (request.isOverride() ? "覆盖" : "新增") + ")-结束(用时: {} 毫秒) +++++++++++++++++++++++++++\n{} -> {}", usedTime, saveKey, path.toFile().getAbsolutePath());
            return request;
        } catch (IOException e) {
            throw ExceptionHelper.wrappedRuntimeException(e);
        }
    }

    // ============================================ 文件下载 ============================================
    public void download(FileDownloadRequest request) {
        final List<String> saveKeys = request.getSaveKeys();
        final String downloadName = request.getDownloadName();

        try {
            // 获取可下载的文件列表
            final List<FileDownloadEntry> entries = downloadInterceptor.keysTransform(saveKeys, properties.getLocation());

            // 单个文件下载
            if (entries.size() == 1) {
                final FileDownloadEntry entry = entries.get(0);
                RequestContextHelper.download(entry.getData(), Objects.isNull(downloadName) ? entry.getName() : downloadName);
            }

        } catch (Exception e) {
            throw ExceptionHelper.wrappedRuntimeException(e);
        }
    }

}
