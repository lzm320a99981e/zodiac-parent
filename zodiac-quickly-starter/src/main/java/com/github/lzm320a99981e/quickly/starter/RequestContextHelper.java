package com.github.lzm320a99981e.quickly.starter;

import com.google.common.collect.Maps;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 请求上下文辅助类
 */
public class RequestContextHelper {
    private static final String REQUEST_ATTRIBUTE_CACHE_PARAMETER_MAP_KEY = "REQUEST_ATTRIBUTE_CACHE_PARAMETER_MAP_KEY";
    private static final String REQUEST_ATTRIBUTE_CACHE_HEADER_MAP_KEY = "REQUEST_ATTRIBUTE_CACHE_HEADER_MAP_KEY";
    private static final String REQUEST_ATTRIBUTE_CACHE_IP_KEY = "REQUEST_ATTRIBUTE_CACHE_IP_KEY";

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 获取令牌参数
     *
     * @return
     */
    public Map<String, Object> getTokenBody() {
        return (Map<String, Object>) getRequest().getAttribute(Constants.REQUEST_ATTRIBUTE_TOKEN_BODY);
    }

    /**
     * 请求参数
     *
     * @return
     */
    public static Map<String, Object> getParameterMap() {
        HttpServletRequest request = getRequest();
        Object cacheValue = request.getAttribute(REQUEST_ATTRIBUTE_CACHE_PARAMETER_MAP_KEY);
        if (Objects.nonNull(cacheValue)) {
            return (Map<String, Object>) cacheValue;
        }

        Map<String, Object> filteredParameters = Maps.newHashMap();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            if (Objects.isNull(key) || Objects.isNull(value)) {
                continue;
            }

            if (value.length == 1) {
                filteredParameters.put(key, value[0]);
                continue;
            }

            filteredParameters.put(key, value);
        }

        request.setAttribute(REQUEST_ATTRIBUTE_CACHE_PARAMETER_MAP_KEY, filteredParameters);
        return filteredParameters;
    }

    /**
     * 请求头
     *
     * @return
     */
    public static Map<String, Object> getHeaderMap() {
        HttpServletRequest request = getRequest();
        Object cacheValue = request.getAttribute(REQUEST_ATTRIBUTE_CACHE_HEADER_MAP_KEY);
        if (Objects.nonNull(cacheValue)) {
            return (Map<String, Object>) cacheValue;
        }

        Enumeration<String> names = request.getHeaderNames();
        Map<String, Object> headerMap = Maps.newLinkedHashMap();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String header = request.getHeader(name);
            headerMap.put(name, header);
        }

        request.setAttribute(REQUEST_ATTRIBUTE_CACHE_HEADER_MAP_KEY, headerMap);
        return headerMap;
    }

    /**
     * 请求IP，可能包含多个
     *
     * @return
     */
    public static String getRequestIP() {
        HttpServletRequest request = getRequest();
        Object cacheValue = request.getAttribute(REQUEST_ATTRIBUTE_CACHE_IP_KEY);
        if (Objects.nonNull(cacheValue)) {
            return (String) cacheValue;
        }

        List<String> ipHeads = Arrays.asList("X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP");
        String unknown = "unknown";
        String ip;
        // proxy
        for (String ipHead : ipHeads) {
            ip = request.getHeader(ipHead);
            if (null == ip || ip.isEmpty() || unknown.equalsIgnoreCase(ip)) {
                continue;
            }
            request.setAttribute(REQUEST_ATTRIBUTE_CACHE_IP_KEY, ip);
            return ip;
        }

        // nginx
        ip = request.getHeader("X-Real-IP");
        if (null != ip && !ip.isEmpty()) {
            String[] items = ip.split(",");
            for (String item : items) {
                if (!unknown.equalsIgnoreCase(item)) {
                    request.setAttribute(REQUEST_ATTRIBUTE_CACHE_IP_KEY, ip);
                    return ip;
                }
            }
        }

        // other
        ip = request.getRemoteAddr();
        request.setAttribute(REQUEST_ATTRIBUTE_CACHE_IP_KEY, ip);
        return ip;
    }

    /**
     * 获取第一个请求IP,第一个为客户端IP
     *
     * @return
     */
    public static String getRequestFirstIp() {
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
            final ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
