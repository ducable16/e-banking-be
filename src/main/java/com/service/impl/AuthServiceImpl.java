package com.service.impl;

import com.enums.AccountStatus;
import com.model.User;
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
import com.service.JwtService;
import com.service.OtpService;
import com.service.base.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

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
    public UserResponse register(SignUpOTPRequest request) {
//        if(!otpService.validateOtp(request.getEmail(), request.getOtp())) {
//            throw new WrongOtpException();
//        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .account(request.getAccount() == null ? generateAccountNumber() : request.getAccount())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .balance(0L)
                .createdAt(LocalDateTime.now())
                .status(AccountStatus.ACTIVE)
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);
        UserResponse response = new UserResponse(user);
//        System.out.println("saved user: " + user);
        return response;
    }

    @Override
    public TokenResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) throw new WrongOtpException();
        return jwtService.generateTokenWithUserDetails(user);
    }

    @Override
    public String forgetPassword(ForgetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if(passwordEncoder.matches(user.getPassword(), user.getPassword())) {
            return "continue...";
        }
        else return "continue...";
    }

    private String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = createRandomAccountNumber();
        } while (userRepository.existsByAccount(accountNumber));
        return accountNumber;
    }

    private String createRandomAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        sb.append(random.nextInt(9) + 1);

        for (int i = 1; i < 10; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
