package com.mistervoiture.auth.request;

import lombok.Data;

@Data
public class UpdateSecurityPwdReq {
    private String old_pwd;
    private String new_pwd;
}