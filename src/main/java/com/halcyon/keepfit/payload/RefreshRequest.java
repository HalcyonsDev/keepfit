package com.halcyon.keepfit.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshRequest {
    private String refreshToken;
}
