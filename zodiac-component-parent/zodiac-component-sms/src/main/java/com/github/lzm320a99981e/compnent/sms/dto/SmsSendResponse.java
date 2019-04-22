package com.github.lzm320a99981e.compnent.sms.dto;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * 短信发送响应参数
 */
public interface SmsSendResponse {
    /**
     * 响应内容
     *
     * @return
     */
    default String getResponseText() {
        return JSON.toJSONString(this);
    }

    /**
     * 发送是否成功
     *
     * @return
     */
    boolean success();

    /**
     * 将响应内容解析成一个对象
     *
     * @param type
     * @param <T>
     * @return
     */
    default <T> T parseObject(Class<T> type) {
        return JSON.parseObject(getResponseText(), type);
    }

    /**
     * 将相应内容解析成一个数组
     *
     * @param type
     * @param <T>
     * @return
     */
    default <T> List<T> parseArray(Class<T> type) {
        return JSON.parseArray(getResponseText(), type);
    }
}
