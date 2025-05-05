package com.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class NotificationService {

    private final EmailService emailService;

    public String formatCurrencyWithComma(long amount) {
        return String.format("%,d", amount);
    }

    public String notificationFormat(long amount, String account, String message, long remainBalance, String day, String hour,
                               boolean isSender) {
        String stringAmount = formatCurrencyWithComma(amount);
        String stringRemainBalance = formatCurrencyWithComma(remainBalance);
        System.out.println(stringAmount);
        System.out.println(stringRemainBalance);
        System.out.println("------------------------------------------------");
        return "TK " + account + " | " + (isSender ? "GD: -" : "GD: +") + stringAmount + " VND " + day + " " + hour + " | SD: "
                + stringRemainBalance + " VND | ND: " + message;
    }

    public void balanceChangingNotification(String email, long amount, String account, String message, long remainBalance, boolean isSender) {
        LocalTime now = LocalTime.now();
        String hour = now.getHour() + ":" + now.getMinute();
        try {
            emailService.sendEmail(email, "Thông báo biến động số dư", notificationFormat(amount, account, message, remainBalance, LocalDate.now().toString(), hour, isSender));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
