package com.response;

import com.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Integer userId;
    private String account;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Long balance;
    private String role;
    private String status;
    private LocalDateTime createdAt;

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.account = user.getAccount();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.balance = user.getBalance();
        this.role = user.getRole().toString();
        this.status = user.getStatus() != null ? user.getStatus().toString() : null;
        this.createdAt = user.getCreatedAt();
    }

}
