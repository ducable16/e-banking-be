package com.service.impl;

import com.entity.User;
import com.enums.Role;
import com.exception.EmailExistsException;
import com.exception.EntityNotFoundException;
import com.exception.WrongOtpException;
import com.repository.UserRepository;
import com.request.ForgetPasswordRequest;
import com.request.LoginRequest;
import com.request.SignUpOTPRequest;
import com.request.SignUpRequest;
import com.response.TokenResponse;
import com.service.JwtService;
import com.service.OtpService;
import com.service.base.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final OtpService otpService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;


    public void validateNewUser(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailExistsException();
        }
    }
    @Override
    public User register(SignUpOTPRequest request) {
        if(!otpService.validateOtp(request.getEmail(), request.getOtp())) {
            throw new WrongOtpException();
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .role(Role.CUSTOMER)
                .build();
        userRepository.save(user);
        return user;
    }

    @Override
    public TokenResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException());
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) throw new WrongOtpException();
        return jwtService.generateTokenWithUserDetails(user);
    }

    @Override
    public String forgetPassword(ForgetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new EntityNotFoundException());
        if(passwordEncoder.matches(user.getPassword(), user.getPassword())) {
            return "continue...";
        }
        else return "continue...";
    }
}
