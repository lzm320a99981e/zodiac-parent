package com.github.lzm320a99981e.compnent.sms;

import com.github.lzm320a99981e.compnent.sms.dto.SmsSendRequest;
import com.github.lzm320a99981e.compnent.sms.dto.SmsSendResponse;

public interface SmsApi {
    SmsSendResponse send(SmsSendRequest request);
}
