package com.mistervoiture.auth.request;

import com.mistervoiture.auth.beans.Device;
import com.mistervoiture.auth.beans.Location;
import com.mistervoiture.auth.beans.MetaData;
import com.mistervoiture.auth.interfaces.IBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneralRequest {
    private MetaData metadata;
    private LoginReq login;
    private AuthReq auth;
    private String userid;
    private UsernameUniqueReq usernameunique;
    private RefreshTokenReq refreshtoken;
    private ValidationCodePhoneNumber validationCodePhoneNumber;
    private UpdateSecurityPwdReq updateSecurityPwdReq;
    private Date timestamp;
}
