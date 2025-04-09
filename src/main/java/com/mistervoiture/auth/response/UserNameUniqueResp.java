package com.mistervoiture.auth.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserNameUniqueResp {
    boolean is_username_unique;
    String message;
    List<String> usernames;
}
