package com.service.base;

import com.model.User;
import com.request.ForgetPasswordRequest;
import com.request.LoginRequest;
import com.request.SignUpOTPRequest;
import com.response.TokenResponse;
import com.response.UserResponse;

public interface AuthService {

    UserResponse register(SignUpOTPRequest request);

    TokenResponse authenticate(LoginRequest request);

    String forgetPassword(ForgetPasswordRequest request);
}
