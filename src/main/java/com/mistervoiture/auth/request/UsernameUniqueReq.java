package com.mistervoiture.auth.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UsernameUniqueReq {
    private String username;
    private String firstname;
    private String lastname;
    private String full_name;
    private boolean isPhone;
    private String type;
}