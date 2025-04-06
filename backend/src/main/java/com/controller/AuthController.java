package com.controller;

import com.entity.User;
import com.request.LoginRequest;
import com.request.SignUpOTPRequest;
import com.request.SignUpRequest;
import com.request.TokenRefreshRequest;
import com.response.StatusResponse;
import com.response.TokenResponse;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@RequestMapping("/auth")
@RestController
public class AuthController {
    @Autowired
    private OtpService otpService;
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public Object signUp(@RequestBody SignUpRequest request) throws UnsupportedEncodingException {
        authService.validateNewUser(request);
        otpService.generateOtp(request.getEmail(), "Mã xác thực đăng ký.");
        return "Otp was sent to your email";
    }
    @PostMapping("/signup-otp")
    public User signUpOtp(@RequestBody SignUpOTPRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public TokenResponse authenticate(@RequestBody LoginRequest request) {
        return authService.authenticate(request);
    }
}
