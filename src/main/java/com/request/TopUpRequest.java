package com.request;

import lombok.Data;

@Data
public class TopUpRequest {
    private Integer userId;

    private Long amount;
}
