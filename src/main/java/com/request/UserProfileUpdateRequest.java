package com.request;

import com.enums.Role;
import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private Integer userId;

    private String email;

    private String fullName;

    private String phoneNumber;

    private String address;

    Role role;
}
