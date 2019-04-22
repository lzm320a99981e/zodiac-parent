package com.github.lzm320a99981e.compnent.sms.provider.mysubmail.dto;

import lombok.Data;

@Data
public class MysubmailSmsCommonRequest {
    private String appid;
    private String timestamp;
    private String sign_type;
    private String signature;
    private String tag;
}
