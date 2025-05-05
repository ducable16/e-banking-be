package com.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DashboardStatsResponse {
    private long totalUsers;
    private long activeUsers;
    private long lockedUsers;
    private long totalTransactions;
    private long totalBalance;
    private List<TransactionDto> recentTransactions;

    @Data
    @AllArgsConstructor
    public static class UserDto {
        private String fullName;
        private String account;
    }

    @Data
    @Builder
    public static class TransactionDto {
        private Integer transactionId;
        private UserDto sender;
        private UserDto receiver;
        private Long amount;
        private LocalDateTime createdAt;
    }
}
