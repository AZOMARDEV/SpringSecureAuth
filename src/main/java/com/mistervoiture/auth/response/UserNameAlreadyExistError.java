package com.mistervoiture.auth.response;

import java.time.LocalDateTime;
import java.util.List;

public record UserNameAlreadyExistError(
        String path,
        String message,
        int statusCode,
        LocalDateTime localDateTime,
        List<String> usernames
) {
}