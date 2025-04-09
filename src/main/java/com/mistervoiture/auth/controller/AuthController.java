package com.mistervoiture.auth.controller;


import com.mistervoiture.auth.request.DeviceReq;
import com.mistervoiture.auth.request.GeneralRequest;
import com.mistervoiture.auth.response.*;
import com.mistervoiture.auth.services.AuthServices;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthServices authServices;

    public AuthController(AuthServices authServices) {
        this.authServices = authServices;
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody GeneralRequest request){
        DeleteUser response = authServices.deleteUser(request.getUserid(), request.getMetadata());
        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody GeneralRequest request){
        LoginResp loginResp = authServices.login(request.getLogin(), request.getMetadata());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, loginResp.accessToken())
                .body(loginResp);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody GeneralRequest request){
        AuthResp authResp = authServices.registerAuth(request.getAuth(), request.getMetadata());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authResp.accessToken())
                .body(authResp);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@NonNull HttpServletRequest request, @RequestBody GeneralRequest refreshTokenReq) {
        log.info("Refresh token");
        RefreshTokenResp authResponse = authServices.refreshToken(request , refreshTokenReq.getRefreshtoken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authResponse.accessToken())
                .body(authResponse);
    }


    @PostMapping("/device")
    public ResponseEntity<?> InsertDevice(@NonNull HttpServletRequest request , @RequestBody DeviceReq refreshTokenReq){
        return ResponseEntity.ok()
                .body(authServices.insertDevice(request , refreshTokenReq));
    }

    @PostMapping("/isUsernameUnique")
    public ResponseEntity<?> isUsernameUnique(@RequestBody GeneralRequest request){
        return ResponseEntity.ok()
                .body(authServices.userNameUniqueResp(request.getUsernameunique()));
    }

    @PostMapping("/isPhoneValidANDUnique")
    public ResponseEntity<?> isPhoneValidANDUnique(@RequestBody GeneralRequest request){
        return ResponseEntity.ok()
                .body(authServices.isPhoneValidANDUnique(request.getUsernameunique()));
    }

    @PostMapping("/valid/codePhone")
    public ResponseEntity<?> validCodePhone(@RequestBody GeneralRequest request){
        return ResponseEntity.ok()
                .body(authServices.validCodePhone(request.getValidationCodePhoneNumber()));
    }

}
