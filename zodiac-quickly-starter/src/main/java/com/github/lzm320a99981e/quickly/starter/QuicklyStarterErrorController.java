package com.github.lzm320a99981e.quickly.starter;


import com.github.lzm320a99981e.quickly.starter.api.ApiException;
import com.github.lzm320a99981e.quickly.starter.api.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 将一些全局异常处理器捕获不到的异常抛出来，交给全局的异常处理器来处理
 */
@Controller(Constants.BEAN_NAME_PREFIX + "QuicklyStarterErrorController")
@RequestMapping("${server.error.path:${error.path:/error}}")
public class QuicklyStarterErrorController implements ErrorController {

    @Value("${server.error.path:${error.path:/error}}")
    private String path;

    @RequestMapping
    public void error(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        if (Objects.nonNull(exception)) {
            if (NestedServletException.class.isAssignableFrom(exception.getClass())) {
                throw ((NestedServletException) exception).getRootCause();
            }
            throw exception;
        }

        // 统一返回状态码为成功
        response.setStatus(HttpStatus.OK.value());
        HttpStatus status = getStatus(request);
        if (status == HttpStatus.NOT_FOUND) {
            throw new ApiException(ApiResponse.invalidRequestUrl());
        } else {
            throw new ApiException(ApiResponse.error());
        }
    }


    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Override
    public String getErrorPath() {
        return path;
    }
}
