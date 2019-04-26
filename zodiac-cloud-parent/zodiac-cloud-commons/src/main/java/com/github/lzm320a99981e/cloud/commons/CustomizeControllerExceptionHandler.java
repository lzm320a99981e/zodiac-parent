package com.github.lzm320a99981e.cloud.commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 控制器层异常处理器
 *
 * @param <T>
 */
public interface CustomizeControllerExceptionHandler<T> {
    T handle(HttpServletRequest request, HttpServletResponse response, Exception e);
}
