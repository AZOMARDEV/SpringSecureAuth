package com.mistervoiture.auth.request;

import lombok.Data;

@Data
public class RefreshTokenReq {
    private String refreshToken;
}