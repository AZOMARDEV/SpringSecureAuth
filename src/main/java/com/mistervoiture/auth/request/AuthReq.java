package com.mistervoiture.auth.request;

import lombok.Data;

@Data
public class AuthReq {
    private String fullname;
    private String firstname;
    private String lastname;
    private String username;
    private String pwd;
}
