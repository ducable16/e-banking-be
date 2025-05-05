package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.request.*;
import com.response.TokenResponse;
import com.response.UserResponse;
import com.service.*;
import com.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class AuthController {
    private final OtpService otpService;

    private final AuthService authService;

    @GetMapping("/test")
    public Map<String, Object> debug() {
        Map<String, Object> res = new HashMap<>();
        res.put("now", LocalDateTime.now());
        return res;
    }

    @PostMapping("/signup")
    public Object signUp(@RequestBody SignUpRequest request) throws UnsupportedEncodingException {
        authService.validateNewUser(request);
        otpService.generateOtp(request.getEmail(), "Mã xác thực đăng ký.");
        return "Otp was sent to your email";
    }
    @PostMapping("/signup-otp")
    public Object signUpOtp(@RequestBody SignUpOTPRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public Object authenticate(@RequestBody LoginRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/forgot-password")
    public Object forgetPassword(@RequestBody ForgetPasswordRequest request) {
        return authService.forgetPassword(request);
    }
    @PostMapping("/reset-password")
    public Object resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return "Reset password sent to your email";
    }
}
