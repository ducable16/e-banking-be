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
import java.util.Random;

@AllArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final OtpService otpService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private static final int ACCOUNT_LENGTH = 10;
    private static final String DIGITS = "0123456789";


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
                .account(generateUniqueAccountNumber())
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

    public String generateUniqueAccountNumber() {
        String accountNumber;
        Random random = new Random();

        do {
            accountNumber = generateRandomDigits(random);
        } while (userRepository.existsByAccount(accountNumber));

        return accountNumber;
    }

    private String generateRandomDigits(Random random) {
        StringBuilder sb = new StringBuilder(ACCOUNT_LENGTH);
        for (int i = 0; i < ACCOUNT_LENGTH; i++) {
            sb.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        return sb.toString();
    }
}
