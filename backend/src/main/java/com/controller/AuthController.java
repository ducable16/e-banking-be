package com.controller;

import com.entity.User;
import com.request.*;
import com.response.TokenResponse;
import com.service.*;
import com.service.impl.AuthServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class AuthController {
    private final OtpService otpService;

    private final AuthServiceImpl authServiceImpl;

    @PostMapping("/signup")
    public Object signUp(@RequestBody SignUpRequest request) throws UnsupportedEncodingException {
        authServiceImpl.validateNewUser(request);
        otpService.generateOtp(request.getEmail(), "Mã xác thực đăng ký.");
        return "Otp was sent to your email";
    }
    @PostMapping("/signup-otp")
    public User signUpOtp(@RequestBody SignUpOTPRequest request) {
        return authServiceImpl.register(request);
    }

    @PostMapping("/login")
    public TokenResponse authenticate(@RequestBody LoginRequest request) {
        return authServiceImpl.authenticate(request);
    }

    @PostMapping("/forget-password")
    public Object forgetPassword(@RequestBody ForgetPasswordRequest request) {
        return authServiceImpl.forgetPassword(request);
    }
}
