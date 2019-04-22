package com.github.lzm320a99981e.compnent.sms.provider.mysubmail.dto;

import lombok.Data;

@Data
public class MysubmailSmsSendRequest extends MysubmailSmsCommonRequest {
    private String to;
    private String content;
}
