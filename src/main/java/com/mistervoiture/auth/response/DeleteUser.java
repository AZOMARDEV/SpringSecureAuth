package com.mistervoiture.auth.response;

import lombok.Builder;

@Builder
public record DeleteUser(
        String message ,
        String secretname,
        String username,
        String codeNickname,
        boolean status
) {
}