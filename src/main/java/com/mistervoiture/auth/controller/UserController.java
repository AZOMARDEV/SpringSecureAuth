package com.mistervoiture.auth.controller;

import com.mistervoiture.auth.request.GeneralRequest;
import com.mistervoiture.auth.services.AuthServices;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    @GetMapping("/test_1")
    public ResponseEntity<?> TestApiWithFilter(){

        List<String> list = new ArrayList<>();

        list.add("data1");
        list.add("data2");
        list.add("data3");
        list.add("data4");
        list.add("data5");
        list.add("data6");
        list.add("data7");
        list.add("data8");
        list.add("data9");
        list.add("data10");
        list.add("data11");

        return ResponseEntity.ok()
                .body(list);
    }

    private final AuthServices authServices;

    public UserController(AuthServices authServices) {
        this.authServices = authServices;
    }

    @PostMapping("/update/security/pwd")
    public ResponseEntity<?> UpdateSecurityPass(@NonNull HttpServletRequest request, @RequestBody GeneralRequest generalRequest){
        log.info("UpdateSecurityPass");
        return ResponseEntity.ok()
                .body(authServices.UpdateSecurityPass(request , generalRequest.getUpdateSecurityPwdReq()));
    }
}
