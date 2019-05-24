package com.github.lzm320a99981e.quickly.starter.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Api请求体切面
 */
@Slf4j
@ControllerAdvice
public class ApiRequestBodyAdvice extends RequestBodyAdviceAdapter {
    @Autowired
    private ApiProperties apiProperties;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return supports(methodParameter.getMethod(), apiProperties.getRequestBodyAdvicePackages());
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("\n+++++++++++++++++++++++++ 接口输入参数(" + getMethodDef(parameter.getMethod()) + ") +++++++++++++++++++++++++\n{}", JSON.toJSONString(body, SerializerFeature.PrettyFormat, SerializerFeature.SortField, SerializerFeature.WriteDateUseDateFormat));
        return body;
    }

    /**
     * 方法签名
     *
     * @param method
     * @return
     */
    static String getMethodDef(Method method) {
        StringBuilder logText = new StringBuilder();
        logText.append(method.getReturnType().getSimpleName() + " ")
                .append(method.getDeclaringClass().getName())
                .append(".")
                .append(method.getName())
                .append("(" + (Arrays.asList(method.getParameterTypes()).stream().map(Class::getSimpleName).collect(Collectors.joining(", "))) + ")");
        return logText.toString();
    }

    /**
     * 是否支持请求体处理
     *
     * @param method
     * @param packages
     * @return
     */
    static boolean supports(Method method, List<String> packages) {
        final String className = method.getDeclaringClass().getName();
        if (Objects.isNull(packages) || packages.isEmpty()) {
            return !className.startsWith("springfox.");
        }
        return packages.stream().anyMatch(pkg -> className.startsWith(pkg) || className.matches(pkg));
    }
}
