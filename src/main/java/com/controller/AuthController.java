package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.User;
import com.request.*;
import com.response.ApiResponse;
import com.response.TokenResponse;
import com.response.UserResponse;
import com.service.*;
import com.service.impl.AuthServiceImpl;
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

    private final AuthServiceImpl authServiceImpl;

    private final ObjectMapper objectMapper;

    @GetMapping("/test")
    public Map<String, Object> debug() {
        Map<String, Object> res = new HashMap<>();
        res.put("now", LocalDateTime.now());
        return res;
    }

    @PostMapping("/signup")
    public Object signUp(@RequestBody SignUpRequest request) throws UnsupportedEncodingException {
        authServiceImpl.validateNewUser(request);
        otpService.generateOtp(request.getEmail(), "Mã xác thực đăng ký.");
        return "Otp was sent to your email";
    }
    @PostMapping("/signup-otp")
    public UserResponse signUpOtp(@RequestBody SignUpOTPRequest request) {
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
