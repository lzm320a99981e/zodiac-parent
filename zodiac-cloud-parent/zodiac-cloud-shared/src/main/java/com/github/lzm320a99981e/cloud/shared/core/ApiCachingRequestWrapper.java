package com.github.lzm320a99981e.cloud.shared.core;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ApiCachingRequestWrapper extends HttpServletRequestWrapper {
    /**
     * 从真实请求对象流里面读取出来的内容，都会被写入到缓存区
     */
    private final ByteArrayOutputStream cached;
    /**
     * 包装原始请求对象的字节输入流
     */
    private ServletInputStream wrappedInput;
    /**
     * 包装原始请求对象的字符输入流
     */
    private BufferedReader wrappedReader;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public ApiCachingRequestWrapper(HttpServletRequest request) {
        super(request);
        final int contentLength = request.getContentLength();
        this.cached = new ByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (Objects.isNull(this.wrappedInput)) {
            this.wrappedInput = new ApiCachingInputStream(getRequest().getInputStream());
        }
        return this.wrappedInput;
    }

    @Override
    public String getCharacterEncoding() {
        final String encoding = super.getCharacterEncoding();
        return Objects.isNull(encoding) ? StandardCharsets.UTF_8.name() : encoding;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (Objects.isNull(this.wrappedReader)) {
            this.wrappedReader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }
        return this.wrappedReader;
    }

    /**
     * 返回缓存内容
     *
     * @return
     */
    public byte[] getCachedContent() {
        return this.cached.toByteArray();
    }

    /**
     * 包装Servlet输入流，将从Servlet输入流读取出来的内容，同时放入到缓存，以便后续使用
     */
    private class ApiCachingInputStream extends ServletInputStream {
        private final ServletInputStream is;

        public ApiCachingInputStream(ServletInputStream is) {
            this.is = is;
        }

        @Override
        public boolean isFinished() {
            return this.is.isFinished();
        }

        @Override
        public boolean isReady() {
            return this.is.isReady();
        }

        @Override
        public void setReadListener(ReadListener listener) {
            this.is.setReadListener(listener);
        }

        @Override
        public int read() throws IOException {
            final int c = this.is.read();
            if (c != -1) {
                // 写入到缓存
                cached.write(c);
            }
            return c;
        }

        @Override
        public int read(byte[] buffer) throws IOException {
            final int count = this.is.read(buffer);
            // 写入到缓存
            writeToCache(buffer, 0, count);
            return count;
        }

        @Override
        public int read(byte[] buffer, int offset, int length) throws IOException {
            final int count = this.is.read(buffer, offset, length);
            // 写入到缓存
            writeToCache(buffer, offset, count);
            return count;
        }

        @Override
        public int readLine(byte[] buffer, int offset, int length) throws IOException {
            final int count = this.is.readLine(buffer, offset, length);
            // 写入到缓存
            writeToCache(buffer, offset, count);
            return count;
        }

        /**
         * 写入到缓存
         *
         * @param bytes
         * @param offset
         * @param length
         */
        private void writeToCache(final byte[] bytes, final int offset, int length) {
            if (length > 0) {
                cached.write(bytes, offset, length);
            }
        }
    }
}
