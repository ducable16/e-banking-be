package com.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionFilterRequest {

    private Integer userId;

    private LocalDate startDate;

    private LocalDate endDate;
}
