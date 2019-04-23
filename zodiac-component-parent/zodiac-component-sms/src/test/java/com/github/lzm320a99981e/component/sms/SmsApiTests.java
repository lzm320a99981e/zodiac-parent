package com.github.lzm320a99981e.component.sms;

import com.alibaba.fastjson.JSON;
import com.github.lzm320a99981e.component.sms.dto.SmsSendRequest;
import com.github.lzm320a99981e.component.sms.dto.SmsSendResponse;
import com.github.lzm320a99981e.component.sms.provider.mysubmail.MysubmailSmsApi;
import com.github.lzm320a99981e.component.sms.provider.mysubmail.MysubmailSmsApiAdapter;
import com.github.lzm320a99981e.component.sms.provider.mysubmail.MysubmailSmsProperties;
import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.Arrays;

public class SmsApiTests {
    static final String appid = "32902";
    static final String appkey = "78f4d376fa9db1f5fef9b91417b92dd";

    @Test
    public void test() {
        final MysubmailSmsProperties properties = new MysubmailSmsProperties();
        properties.setAppid(appid);
        properties.setAppkey(appkey);

        final MysubmailSmsApi mysubmailSmsApi = new MysubmailSmsApi(properties);

        SmsApi smsApi = new MysubmailSmsApiAdapter(mysubmailSmsApi);
        final SmsSendRequest request = new SmsSendRequest();
        request.setProvider(Provider.Mysubmail.name());
        request.setSignature("【GSPS】");
        request.setTemplate("{captcha}为您的登录验证码，请于5分钟内填写。如非本人操作，请忽略本短信。");
        request.setReceivers(Arrays.asList("13380384850"));
        request.setParameterMap(Splitter.on("&").withKeyValueSeparator("=").split("captcha=112233"));
        final SmsSendResponse response = smsApi.send(request);
        System.out.println(JSON.toJSONString(response, true));

    }
}
