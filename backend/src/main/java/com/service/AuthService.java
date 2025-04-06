package com.service;

import com.entity.User;
import com.enums.Role;
import com.exception.EmailExistsException;
import com.exception.UserNotFoundException;
import com.exception.WrongOtpException;
import com.repository.UserRepository;
import com.request.LoginRequest;
import com.request.SignUpOTPRequest;
import com.request.SignUpRequest;
import com.response.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpService otpService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public void validateNewUser(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailExistsException();
        }
    }
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

    public TokenResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException());
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) throw new WrongOtpException();
        return jwtService.generateTokenWithUserDetails(user);
    }
}
