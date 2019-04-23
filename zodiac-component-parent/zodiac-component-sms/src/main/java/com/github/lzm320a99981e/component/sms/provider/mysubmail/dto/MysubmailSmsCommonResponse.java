package com.github.lzm320a99981e.component.sms.provider.mysubmail.dto;

import com.github.lzm320a99981e.component.sms.dto.SmsSendResponse;
import lombok.Data;

import java.util.Objects;

@Data
public class MysubmailSmsCommonResponse implements SmsSendResponse {
    private String status;
    private String code;
    private String msg;
    private String send_id;
    private String fee;
    private String sms_credits;

    @Override
    public boolean success() {
        return Objects.equals("success", status);
    }
}
