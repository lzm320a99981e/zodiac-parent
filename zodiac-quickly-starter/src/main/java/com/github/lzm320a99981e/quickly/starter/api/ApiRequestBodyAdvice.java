package com.github.lzm320a99981e.quickly.starter.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ApiRequestBodyAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !methodParameter.getMethod().getDeclaringClass().getName().startsWith("springfox.");
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("\n+++++++++++++++++++++++++ 接口输入参数(" + getMethodDef(parameter.getMethod()) + ") +++++++++++++++++++++++++\n{}", JSON.toJSONString(body, SerializerFeature.PrettyFormat, SerializerFeature.SortField, SerializerFeature.WriteDateUseDateFormat));
        return body;
    }

    static String getMethodDef(Method method) {
        StringBuilder logText = new StringBuilder();
        logText.append(method.getReturnType().getSimpleName() + " ")
                .append(method.getDeclaringClass().getName())
                .append(".")
                .append(method.getName())
                .append("(" + (Arrays.asList(method.getParameterTypes()).stream().map(Class::getSimpleName).collect(Collectors.joining(", "))) + ")");
        return logText.toString();
    }
}
