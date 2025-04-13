package com.request;

import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private Integer userId;

    private String fullName;

    private String phoneNumber;

    private String address;
}
