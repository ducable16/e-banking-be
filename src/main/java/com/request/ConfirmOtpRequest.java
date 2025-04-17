package com.request;

import lombok.Data;

@Data
public class ConfirmOtpRequest {
    private String email;
    private String otp;
}
