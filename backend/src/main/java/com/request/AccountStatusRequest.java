package com.request;

import com.enums.AccountStatus;
import lombok.Data;

@Data
public class AccountStatusRequest {

    private Integer userId;

    private String username;

    private AccountStatus status;
}
