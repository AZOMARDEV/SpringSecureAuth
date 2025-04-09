package com.mistervoiture.auth.response;

import com.mistervoiture.auth.enums.BearerID;
import com.mistervoiture.auth.enums.CredentialStatus;
import lombok.Builder;

@Builder
public record LoginResp (
        CredentialStatus credential,
        BearerID bearer,
        String accessToken,
        String refreshToken,
        String username,
        String secretusername
) {
}
