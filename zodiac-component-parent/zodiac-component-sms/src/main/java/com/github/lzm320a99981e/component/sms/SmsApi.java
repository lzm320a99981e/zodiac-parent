package com.github.lzm320a99981e.component.sms;

import com.github.lzm320a99981e.component.sms.dto.SmsSendRequest;
import com.github.lzm320a99981e.component.sms.dto.SmsSendResponse;

public interface SmsApi {
    SmsSendResponse send(SmsSendRequest request);
}
