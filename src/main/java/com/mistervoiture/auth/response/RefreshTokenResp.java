package com.mistervoiture.auth.response;


import lombok.Builder;

@Builder
public record RefreshTokenResp(
        String refreshToken,
        String accessToken,
        String username
) {
}

