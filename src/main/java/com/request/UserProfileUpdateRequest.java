package com.request;

import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private Integer userId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String address;
}
