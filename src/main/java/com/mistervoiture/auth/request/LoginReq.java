package com.mistervoiture.auth.request;

import com.mistervoiture.auth.enums.CredentialStatus;
import com.mistervoiture.auth.interfaces.IBean;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginReq implements IBean {
    private CredentialStatus credential;
    private String username;
    private String pwd;
}
