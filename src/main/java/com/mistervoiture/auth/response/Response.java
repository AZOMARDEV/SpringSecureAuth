package com.mistervoiture.auth.response;

import lombok.Builder;

@Builder
public record Response(
        String message ,
        boolean status
) {
}