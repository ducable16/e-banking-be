package com.service;

import com.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public List<Map<String, Object>> getLast5MonthsSummary(Integer userId) {
        List<Object[]> results = transactionRepository.getMonthlyStatsLast5Months(userId);

        List<Map<String, Object>> summary = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("month", ((Timestamp) row[0]).toLocalDateTime().toLocalDate().withDayOfMonth(1));
            map.put("totalSent", row[1]);
            map.put("totalReceived", row[2]);
            summary.add(map);
        }
        return summary;
    }
}

