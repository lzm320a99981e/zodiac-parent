package com.github.lzm320a99981e.compnent.sms.dto;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 短信发送请求参数
 */
@Data
public class SmsSendRequest {
    private static Map<String, String> templateVariablePairMap = new LinkedHashMap<>();

    static {
        templateVariablePairMap.put("${", "}");
        templateVariablePairMap.put("{{", "}}");
        templateVariablePairMap.put("{", "}");
    }

    /**
     * 提供商
     */
    private String provider;
    /**
     * 签名
     */
    private String signature;
    /**
     * 模板
     */
    private String template;
    /**
     * 模板参数
     */
    private Map<String, String> parameterMap;
    /**
     * 接收方
     */
    private List<String> receivers;

    /**
     * 模板解析
     *
     * @return
     */
    public String parseTemplate() {
        if (Objects.isNull(this.parameterMap) || this.parameterMap.isEmpty()) {
            return this.template;
        }
        String parsed = this.template;
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            for (Map.Entry<String, String> pair : templateVariablePairMap.entrySet()) {
                parsed = parsed.replace(pair.getKey() + entry.getKey() + pair.getValue(), entry.getValue());
            }
        }
        return parsed;
    }
}
