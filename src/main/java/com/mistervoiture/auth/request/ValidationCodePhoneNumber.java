package com.mistervoiture.auth.request;

import lombok.Data;

@Data
public class ValidationCodePhoneNumber {
    String code;
    String phone;
}