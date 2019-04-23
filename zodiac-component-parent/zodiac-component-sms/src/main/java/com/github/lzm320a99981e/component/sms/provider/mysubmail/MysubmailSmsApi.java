package com.github.lzm320a99981e.component.sms.provider.mysubmail;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.lzm320a99981e.component.sms.provider.mysubmail.dto.MysubmailSmsCommonRequest;
import com.github.lzm320a99981e.component.sms.provider.mysubmail.dto.MysubmailSmsCommonResponse;
import com.github.lzm320a99981e.component.sms.provider.mysubmail.dto.MysubmailSmsSendRequest;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信调用接口
 */
public class MysubmailSmsApi {
    private final MysubmailSmsHttp http;
    private final MysubmailSmsProperties properties;

    public MysubmailSmsApi(MysubmailSmsProperties properties) {
        this.properties = properties;
        this.http = new Retrofit.Builder()
                .baseUrl(properties.getUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MysubmailSmsHttp.class);
    }

    public MysubmailSmsCommonResponse send(MysubmailSmsSendRequest request) {
        setDefaults(request);
        return exe(http.send(toRequestBodyMap(request))).body();
    }

    private void setDefaults(MysubmailSmsCommonRequest request) {
        request.setAppid(properties.getAppid());
        request.setSignature(properties.getAppkey());
    }

    private Map<String, RequestBody> toRequestBodyMap(Object request) {
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        JSONObject parameters = JSON.parseObject(JSON.toJSONString(request));
        MediaType mediaType = MediaType.parse("multipart/form-data");
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            requestBodyMap.put(entry.getKey(), RequestBody.create(mediaType, String.valueOf(entry.getValue())));
        }
        return requestBodyMap;
    }

    private <T> Response<T> exe(Call<T> call) {
        try {
            return call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}