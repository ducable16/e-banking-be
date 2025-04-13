package com.request;

import com.enums.Role;
import lombok.Data;
import lombok.Getter;


@Data
public class SignUpOTPRequest {
    private String email;
    private String account;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Role role;
    private String otp;
}
