package com.github.lzm320a99981e.quickly.starter.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;


@Slf4j
@ControllerAdvice
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getMethod().getDeclaringClass().getName().startsWith("springfox.");
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.info("\n+++++++++++++++++++++++++ 接口输出参数(" + ApiRequestBodyAdvice.getMethodDef(returnType.getMethod()) + ") +++++++++++++++++++++++++\n{}", JSON.toJSONString(body, SerializerFeature.PrettyFormat, SerializerFeature.SortField, SerializerFeature.WriteDateUseDateFormat));
        // 空响应
        if (Objects.isNull(body)) {
            return ApiResponse.success();
        }

        // 错误响应
        if (ApiResponse.class.isAssignableFrom(body.getClass())) {
            return body;
        }

        // 正常响应
        return ApiResponse.success(body);
    }


}
