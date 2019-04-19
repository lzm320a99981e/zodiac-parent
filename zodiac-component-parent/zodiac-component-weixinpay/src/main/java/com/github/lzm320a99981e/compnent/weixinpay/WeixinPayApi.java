package com.github.lzm320a99981e.compnent.weixinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.lzm320a99981e.compnent.weixinpay.dto.*;
import com.github.lzm320a99981e.zodiac.tools.ExceptionHelper;
import com.github.lzm320a99981e.zodiac.tools.IdGenerator;
import com.github.lzm320a99981e.zodiac.tools.XmlTransfer;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 微信支付相关接口
 */
@Slf4j
public class WeixinPayApi {
    /**
     * 配置信息
     */
    private final WeixinPayProperties properties;
    /**
     * 请求客户端
     */
    private final WeixinPayHttp http;

    /**
     * 初始化
     *
     * @param properties
     */
    public WeixinPayApi(WeixinPayProperties properties) {
        this.properties = properties;
        this.http = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeixinPayHttp.class);
    }

    /**
     * 统一下单
     *
     * @param request
     * @return
     */
    public UnifiedorderResponse unifiedorder(UnifiedorderRequest request) {
        request.setNotify_url(properties.getNotify_url());
        request.setSpbill_create_ip(properties.getSpbill_create_ip());
        final UnifiedorderRequest finalRequest = setCommons(request);
        // 签名
        finalRequest.setSign(WeixinPayHelper.sign(finalRequest, properties.getApikey()));
        // 发起请求
        return doRequest(finalRequest, properties.getApiUrl().getUnifiedorder(), UnifiedorderResponse.class);
    }


    /**
     * 查询订单
     *
     * @param request
     * @return
     */
    public OrderqueryResponse orderquery(OrderqueryRequest request) {
        // 设置公共参数
        final OrderqueryRequest finalRequest = setCommons(request);
        // 签名
        finalRequest.setSign(WeixinPayHelper.sign(finalRequest, properties.getApikey()));
        // 发起请求
        return doRequest(finalRequest, properties.getApiUrl().getOrderquery(), OrderqueryResponse.class);
    }

    /**
     * 关闭订单
     *
     * @param request
     * @return
     */
    public CloseorderResponse closeorder(CloseorderRequest request) {
        // 设置公共参数
        final CloseorderRequest finalRequest = setCommons(request);
        // 签名
        finalRequest.setSign(WeixinPayHelper.sign(finalRequest, properties.getApikey()));
        // 发起请求
        return doRequest(finalRequest, properties.getApiUrl().getCloseorder(), CloseorderResponse.class);
    }

    /**
     * 设置公共参数
     *
     * @param request
     * @param <T>
     * @return
     */
    private <T> T setCommons(T request) {
        final JSONObject parameters = JSON.parseObject(JSON.toJSONString(request));
        parameters.put("appid", properties.getAppid());
        parameters.put("mch_id", properties.getMch_id());
        parameters.put("nonce_str", IdGenerator.uuid32());
        return parameters.toJavaObject((Type) request.getClass());
    }

    /**
     * 向微信端发送请求
     *
     * @param parameters
     * @param apiUrl
     * @param responseType
     * @param <T>
     * @return
     */
    private <T> T doRequest(Object parameters, String apiUrl, Class<T> responseType) {
        try {
            final XmlTransfer xmlTransfer = XmlTransfer.create();
            // 调用微信接口
            final String body = xmlTransfer.toXML(parameters);
            log.info("调用微信支付接口({}) :: 输入参数 -> \n{}", apiUrl, body);
            final Call<ResponseBody> call = http.doExecute(apiUrl, RequestBody.create(MediaType.parse("application/xml;charset=UTF-8"), body));
            final String responseText = Objects.requireNonNull(call.execute().body()).string();
            log.info("调用微信支付接口({}) :: 输出参数 -> \n{}", apiUrl, responseText);
            final T response = xmlTransfer.fromXML(responseText, responseType);
            // 验证签名
            WeixinPayHelper.verifyParametersAndSign(response, properties.getApikey());
            return response;
        } catch (Exception e) {
            ExceptionHelper.rethrowRuntimeException(e);
        }
        return null;
    }
}
