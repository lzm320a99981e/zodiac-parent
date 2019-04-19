package com.github.lzm320a99981e.compnent.weixinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.lzm320a99981e.zodiac.tools.HashUtils;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@Slf4j
public class WeixinPayHelper {
    /**
     * 校验微信返回结果
     *
     * @param parameters
     * @return
     */
    private static void verifyParameters(Object parameters) {
        final JSONObject nonEmptyParameters = JSON.parseObject(JSON.toJSONString(parameters));
        final String successFlag = "SUCCESS";
        final String returnCode = nonEmptyParameters.getString("return_code");
        if (!Objects.equals(successFlag, returnCode)) {
            // 通信异常
            throw new WeixinPayException(returnCode, nonEmptyParameters.getString("return_code"), WeixinPayException.Type.RETURN);
        }
        if (nonEmptyParameters.containsKey("result_code") && !Objects.equals(successFlag, nonEmptyParameters.get("result_code"))) {
            // 业务异常
            final String resultCode = nonEmptyParameters.getString("result_code");
            throw new WeixinPayException(resultCode, nonEmptyParameters.getString("result_msg"), WeixinPayException.Type.RESULT);
        }
    }

    /**
     * 签名
     *
     * @param parameters
     * @param apiKey
     * @return
     */
    public static String sign(Object parameters, String apiKey) {
        final TreeMap signParameters = new TreeMap(filterEmpty(JSON.parseObject(JSON.toJSONString(parameters))));
        final String signTemplate = Joiner.on("&").withKeyValueSeparator("=").join(signParameters) + "&key=" + apiKey;
        final String sign = HashUtils.md5Hash(signTemplate).toUpperCase();
        log.info("微信支付接口 :: 签名 :: 参数 -> \n{}\n最终签名参数 -> \n{}\n生成签名 -> \n{}", JSON.toJSONString(signParameters, true), signTemplate, sign);
        return sign;
    }

    private static Map<String, Object> filterEmpty(Map<String, Object> parameters) {
        final Map<String, Object> filtered = new HashMap<>(parameters);
        parameters.keySet().stream().filter(item -> parameters.get(item).toString().isEmpty()).forEach(filtered::remove);
        return filtered;
    }

    /**
     * 验证签名
     *
     * @param parameters
     * @param apiKey
     * @return
     */
    private static void verifySign(Object parameters, String apiKey) {
        final JSONObject nonEmptyParameters = JSON.parseObject(JSON.toJSONString(parameters));
        final String signKey = "sign";
        final String sign = nonEmptyParameters.getString(signKey);
        nonEmptyParameters.remove(signKey);
        final String verifySign = sign(nonEmptyParameters, apiKey);
        log.info("微信支付接口 :: 验证签名 :: 参数 -> \n{}待校验签名 -> {}\n经计算签名 -> {}", JSON.toJSONString(nonEmptyParameters, true), verifySign, sign);
        if (!Objects.equals(sign, verifySign)) {
            throw new WeixinPayException("SIGNERROR", "签名错误", WeixinPayException.Type.SIGN);
        }
    }

    /**
     * 校验微信端返回的参数和签名
     *
     * @param parameters
     * @param apiKey
     */
    public static void verifyParametersAndSign(Object parameters, String apiKey) {
        verifyParameters(parameters);
        verifySign(parameters, apiKey);
    }
}
