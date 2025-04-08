package com.service.base;

import com.entity.User;
import com.request.ForgetPasswordRequest;
import com.request.LoginRequest;
import com.request.SignUpOTPRequest;
import com.request.SignUpRequest;
import com.response.TokenResponse;

public interface AuthService {

    User register(SignUpOTPRequest request);

    TokenResponse authenticate(LoginRequest request);

    String forgetPassword(ForgetPasswordRequest request);
}
