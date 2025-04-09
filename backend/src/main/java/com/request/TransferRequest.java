package com.request;

import lombok.Data;

@Data
public class TransferRequest {

    private Integer senderId;

    private Integer receiverId;

    private Long amount;

    private String note;
}
