package com.service;

import com.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Map<String, Long> getMonthlySummary(Integer userId, int month, int year) {
        Long totalSent = transactionRepository.getTotalSentByUserInMonth(userId, month, year);
        Long totalReceived = transactionRepository.getTotalReceivedByUserInMonth(userId, month, year);

        Map<String, Long> result = new HashMap<>();
        result.put("totalSent", totalSent);
        result.put("totalReceived", totalReceived);
        return result;
    }
}

