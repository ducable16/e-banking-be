package com.request;

import com.enums.AccountStatus;
import com.enums.Role;
import lombok.Data;

@Data
public class AddUserRequest {
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Role role;
    private AccountStatus status;
}
