package com.zodiac.app.gsps.endpoint.common.dto;

import lombok.Data;

@Data
public class PersonalLoginRequest {
    private String username;
    private String password;
}
