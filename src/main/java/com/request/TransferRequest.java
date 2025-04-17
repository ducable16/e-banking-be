package com.request;

import lombok.Data;

@Data
public class TransferRequest {

    private String fromAccount;

    private String toAccount;

    private Long amount;

    private String note;
}
