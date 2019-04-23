package com.github.lzm320a99981e.component.sms.provider.mysubmail.dto;

import lombok.Data;

@Data
public class MysubmailSmsSendRequest extends MysubmailSmsCommonRequest {
    private String to;
    private String content;
}
