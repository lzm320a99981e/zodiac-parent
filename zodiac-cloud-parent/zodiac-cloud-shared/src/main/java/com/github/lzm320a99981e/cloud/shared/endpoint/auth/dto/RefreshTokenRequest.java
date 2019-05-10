package com.github.lzm320a99981e.cloud.shared.endpoint.auth.dto;

import com.github.lzm320a99981e.component.validation.Check;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @Check
    private String refreshToken;
}
