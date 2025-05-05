package com.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@AllArgsConstructor
@Service
public class OtpService {

    private final EmailService emailService;

    private final Map<String, String> otpStorage = new HashMap<>();
    private final Random random = new Random();

    public String generateOtp(String email, String subject) throws UnsupportedEncodingException {
        String otp = String.format("%06d", random.nextInt(999999));
        otpStorage.put(email, otp);

        emailService.sendEmail(email, subject, "Mã xác thực OTP của bạn là: " + otp + ". Mã này có hiệu lực trong 5 phút.");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                otpStorage.remove(email);
            }
        }, 5 * 60 * 1000);
        return otp;
    }

    public void generateForgetPasswordOtp(String email, String subject) throws UnsupportedEncodingException {
        String otp = String.format("%06d", random.nextInt(999999));
        otpStorage.put(email, otp);

        emailService.sendEmail(email, subject, "Mã OTP để xác minh yêu cầu đặt lại mật khẩu của bạn là: " + otp + ". Mã này có hiệu lực trong 5 phút.");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                otpStorage.remove(email);
            }
        }, 5 * 60 * 1000);
    }

    public boolean validateOtp(String email, String otp) {
        String storedOtp = otpStorage.get(email);
        return storedOtp != null && storedOtp.equals(otp);
    }
}