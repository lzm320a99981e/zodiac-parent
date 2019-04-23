package com.github.lzm320a99981e.component.sms.provider.mysubmail;

import com.github.lzm320a99981e.component.sms.SmsApi;
import com.github.lzm320a99981e.component.sms.dto.SmsSendRequest;
import com.github.lzm320a99981e.component.sms.dto.SmsSendResponse;
import com.github.lzm320a99981e.component.sms.provider.mysubmail.dto.MysubmailSmsSendRequest;

/**
 * 短信调用接口适配器
 */
public class MysubmailSmsApiAdapter implements SmsApi {
    private final MysubmailSmsApi api;

    public MysubmailSmsApiAdapter(MysubmailSmsApi api) {
        this.api = api;
    }

    @Override
    public SmsSendResponse send(SmsSendRequest request) {
        final MysubmailSmsSendRequest rawRequest = new MysubmailSmsSendRequest();
        rawRequest.setContent(request.getSignature() + request.parseTemplate());
        rawRequest.setTo(request.getReceivers().get(0));
        return api.send(rawRequest);
    }
}