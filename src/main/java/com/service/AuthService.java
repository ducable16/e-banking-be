package com.service;

import com.entity.User;
import com.enums.AccountStatus;
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
import com.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final OtpService otpService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;


    public void validateNewUser(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailExistsException();
        }
    }
    public UserResponse register(SignUpOTPRequest request) {
        if(!otpService.validateOtp(request.getEmail(), request.getOtp())) {
            throw new WrongOtpException();
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .balance(0L)
                .createdAt(LocalDateTime.now())
                .status(AccountStatus.ACTIVE)
                .role(Role.CUSTOMER)
                .build();
        userRepository.save(user);
        return new UserResponse(user);
    }

    public TokenResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) throw new WrongOtpException();
        return jwtService.generateTokenWithUserDetails(user);
    }

    public String forgetPassword(ForgetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if(passwordEncoder.matches(user.getPassword(), user.getPassword())) {
            return "continue...";
        }
        else return "continue...";
    }
}
