package com.mistervoiture.auth.services;

import com.mistervoiture.auth.beans.MetaData;
import com.mistervoiture.auth.request.*;
import com.mistervoiture.auth.response.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthServices {
    DeleteUser deleteUser(String userid , MetaData metadata);
    LoginResp login(LoginReq login , MetaData metadata);
    AuthResp registerAuth(AuthReq authReq , MetaData metadata);
    RefreshTokenResp refreshToken(HttpServletRequest request, RefreshTokenReq refreshTokenReq);
    DeviceId insertDevice(HttpServletRequest request , DeviceReq metaData);
    int isNickNameUnique(String nickname);
    int isUserNameUnique(String username);
    UserNameUniqueResp userNameUniqueResp(UsernameUniqueReq username);
    UserNameUniqueResp isPhoneValidANDUnique(UsernameUniqueReq username);
    UserNameUniqueResp validCodePhone(ValidationCodePhoneNumber validationCodePhoneNumber);
    Response UpdateSecurityPass(HttpServletRequest request, UpdateSecurityPwdReq updateSecurityPwdReq);
}
