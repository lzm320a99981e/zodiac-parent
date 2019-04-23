package com.github.lzm320a99981e.component.weixinpay;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 发送请求
 */
public interface WeixinPayHttp {
    @POST
    Call<ResponseBody> doExecute(@Url String url, @Body RequestBody requestBody);
}
