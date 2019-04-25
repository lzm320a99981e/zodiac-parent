package com.github.lzm320a99981e.cloud.shared.core;

import org.springframework.util.FastByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 包装响应对象
 * 暂未处理sendError 和 sendRedirect方法
 * 具体请参考
 *
 * @see org.springframework.web.util.ContentCachingResponseWrapper
 */
public class ApiCachingResponseWrapper extends HttpServletResponseWrapper {

    /**
     * 往真实响应流（ServletOutputStream 和 PrintWriter）写入到内容，都会被重新写入到缓存区
     */
    private final FastByteArrayOutputStream cached = new FastByteArrayOutputStream(1024);

    private ServletOutputStream wrappedOutput;

    private PrintWriter wrappedWriter;

    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response The response to be wrapped
     * @throws IllegalArgumentException if the response is null
     */
    public ApiCachingResponseWrapper(HttpServletResponse response) {
        // 真实响应对象，还是交给父类管理
        super(response);
    }

    @Override
    public String getCharacterEncoding() {
        final String encoding = super.getCharacterEncoding();
        return Objects.isNull(encoding) ? StandardCharsets.UTF_8.name() : encoding;
    }

    /**
     * 对真实响应对象的字节输出流进行包装
     *
     * @return
     * @throws IOException
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (Objects.isNull(this.wrappedOutput)) {
            this.wrappedOutput = new ApiResponseServletOutputStream(getResponse().getOutputStream());
        }
        return wrappedOutput;
    }

    /**
     * 对真实响应对象的字符输出流进行包装
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    @Override
    public PrintWriter getWriter() throws UnsupportedEncodingException {
        if (Objects.isNull(this.wrappedWriter)) {
            this.wrappedWriter = new ApiResponsePrintWriter(getCharacterEncoding());
        }
        return this.wrappedWriter;
    }

    /**
     * 重置缓存区大小
     *
     * @param len
     */
    @Override
    public void setContentLength(int len) {
        if (len > this.cached.size()) {
            // 重置缓存区大小
            this.cached.resize(len);
        }
    }

    /**
     * 重置缓存区大小（覆盖 Servlet 3.1 setContentLengthLong(long length) 接口）
     *
     * @param length
     */
    @Override
    public void setContentLengthLong(long length) {
        if (length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Content-Length exceeds ContentCachingResponseWrapper's maximum (" + Integer.MAX_VALUE + "): " + length);
        }
        this.setContentLength((int) length);
    }

    /**
     * 重置缓存大小
     *
     * @param size
     */
    @Override
    public void setBufferSize(int size) {
        if (size > this.cached.size()) {
            // 重置缓存区大小
            this.cached.resize(size);
        }
    }

    /**
     * 重置缓存（清空缓存区内容）
     */
    @Override
    public void resetBuffer() {
        this.cached.reset();
    }

    /**
     * 获取缓存缓存
     *
     * @return
     */
    public byte[] getCachedContent() {
        return this.cached.toByteArray();
    }

    /**
     * 获取缓存输入流
     *
     * @return
     */
    public InputStream getCachedInputStream() {
        return this.cached.getInputStream();
    }

    /**
     * 获取缓存大小
     *
     * @return
     */
    public int getCachedSize() {
        return this.cached.size();
    }

    // 将缓存区内容写入到真实响应流
    public void writeCachedToResponse() throws IOException {
        copyBodyToResponse();
    }

    /**
     * 重置
     */
    @Override
    public void reset() {
        super.reset();
        this.cached.reset();
    }

    /**
     * 复制缓存区内容到真实的响应流
     *
     * @throws IOException
     */
    private void copyBodyToResponse() throws IOException {
        if (this.cached.size() > 0) {
            // 原始响应对象
            final ServletResponse raw = getResponse();
            // 真实响应流内容还未提交
            if (!raw.isCommitted()) {
                // 设置真实响应流内容长度
                raw.setContentLength(this.cached.size());
            }
            // 将缓存区内容写入到原始响应流
            this.cached.writeTo(raw.getOutputStream());
            // 重置缓存区
            this.cached.reset();
            // 刷新真实响应流缓存
            super.flushBuffer();
        }
    }

    /**
     * 重写响应字节输出流，将原始响应字节流中的内容写入到缓存区
     */
    private class ApiResponseServletOutputStream extends ServletOutputStream {
        private final ServletOutputStream raw;

        private ApiResponseServletOutputStream(ServletOutputStream raw) {
            this.raw = raw;
        }

        @Override
        public boolean isReady() {
            return this.raw.isReady();
        }

        @Override
        public void setWriteListener(WriteListener listener) {
            this.raw.setWriteListener(listener);
        }

        @Override
        public void write(int b) throws IOException {
            cached.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            cached.write(b, off, len);
        }
    }

    /**
     * 重写响应字符输出流，将原始响应字字符流转换为字节流，然后将内容写入到缓存区
     */
    private class ApiResponsePrintWriter extends PrintWriter {
        public ApiResponsePrintWriter(String encoding) throws UnsupportedEncodingException {
            super(new OutputStreamWriter(cached, encoding));
        }

        @Override
        public void write(int c) {
            super.write(c);
            super.flush();
        }

        @Override
        public void write(char[] buf, int off, int len) {
            super.write(buf, off, len);
            super.flush();
        }

        @Override
        public void write(String s, int off, int len) {
            super.write(s, off, len);
            super.flush();
        }
    }
}
