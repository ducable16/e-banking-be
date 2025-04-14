package com.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FakeBillRequest {
    private Integer senderId;
    private Integer receiverId;
    private Long amount;
    private LocalDate createdAt; // chỉ ngày, không có thời gian
}
