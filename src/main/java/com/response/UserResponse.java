package com.response;

import com.model.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Integer userId;
    private String account;
    private String email;
    private String firstName;
    private String lastName;
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
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.balance = user.getBalance();
        this.role = user.getRole().toString();
        this.status = user.getStatus() != null ? user.getStatus().toString() : null;
        this.createdAt = user.getCreatedAt();
    }

}
