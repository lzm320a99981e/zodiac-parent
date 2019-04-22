package com.github.lzm320a99981e.compnent.sms.provider.mysubmail;

import lombok.Data;

@Data
public class MysubmailSmsProperties {
    private String url = "https://api.mysubmail.com/";
    private String appid;
    private String appkey;
}
