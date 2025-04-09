package com.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public String notificationFormat(long amount, String account, String message, long remainBalance, String day, String hour,
                               boolean isSender) {
        return "TK " + account + " | " + (isSender ? "GD: -" : "GD: +") + amount + " VND " + day + " " + hour + " | SD: "
                + remainBalance + " VND | ND: " + message;
    }
}
