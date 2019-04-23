package com.github.lzm320a99981e.component.sms.provider.mysubmail;

import com.github.lzm320a99981e.component.sms.provider.mysubmail.dto.MysubmailSmsCommonResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

import java.util.Map;

/**
 * 短信服务接口
 */
public interface MysubmailSmsHttp {
    @Multipart
    @POST("message/send.json")
    Call<MysubmailSmsCommonResponse> send(@PartMap Map<String, RequestBody> requestBodyMap);
}
