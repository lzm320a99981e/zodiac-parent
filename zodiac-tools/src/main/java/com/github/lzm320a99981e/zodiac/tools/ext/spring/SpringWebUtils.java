package com.github.lzm320a99981e.zodiac.tools.ext.spring;

import com.github.lzm320a99981e.zodiac.tools.ExceptionHelper;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Spring Web 工具类
 */
public abstract class SpringWebUtils {

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 请求IP
     *
     * @return
     */
    public static String getRequestIP() {
        final HttpServletRequest request = getRequest();
        List<String> ipHeads = Arrays.asList("X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP");
        String unknown = "unknown";
        String ip;
        // proxy
        for (String ipHead : ipHeads) {
            ip = request.getHeader(ipHead);
            if (null == ip || ip.isEmpty() || unknown.equalsIgnoreCase(ip)) {
                continue;
            }
            return ip;
        }

        // nginx
        ip = request.getHeader("X-Real-IP");
        if (null != ip && !ip.isEmpty()) {
            String[] items = ip.split(",");
            for (String item : items) {
                if (!unknown.equalsIgnoreCase(item)) {
                    return ip;
                }
            }
        }

        // other
        return request.getRemoteAddr();
    }

    /**
     * 第一个请求IP
     *
     * @return
     */
    public static String getRequestFirstIP() {
        return getRequestIP().split(",")[0];
    }

    /**
     * 下载
     *
     * @param data
     * @param downloadName
     */
    public static void download(byte[] data, String downloadName) {
        try {
            final HttpServletResponse response = getResponse();
            // 设置下载类型
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            // 下载文件名乱码解决
            String fileName = new String(downloadName.getBytes("gbk"), "iso8859-1");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
        } catch (Exception e) {
            ExceptionHelper.rethrowRuntimeException(e);
        }
    }
}
