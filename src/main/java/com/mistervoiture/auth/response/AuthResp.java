package com.mistervoiture.auth.response;

import com.mistervoiture.auth.enums.BearerID;
import com.mistervoiture.auth.enums.CredentialStatus;
import lombok.Builder;

@Builder
public record AuthResp(
        String accessToken,
        String refreshToken,
        String username,
        String avatar,
        String cover,
        String codeNickname,
        String nickname,
        String deviceId,
        BearerID bearerId,
        CredentialStatus credential,
        String languageAuth){
}