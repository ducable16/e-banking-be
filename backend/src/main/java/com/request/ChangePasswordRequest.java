package com.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private Integer userId;
    private String oldPassword;
    private String newPassword;
}
