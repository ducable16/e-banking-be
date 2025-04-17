package com.service;

import com.entity.Transaction;
import com.entity.User;
import com.repository.TransactionRepository;
import com.repository.UserRepository;
import com.request.FakeBillRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;


    private final Random random = new Random();

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

    public Transaction createFakeTransaction(FakeBillRequest request) {
        if (request.getSenderId().equals(request.getReceiverId())) {
            throw new IllegalArgumentException("Người gửi và người nhận không được trùng nhau.");
        }

        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy người gửi"));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy người nhận"));

        LocalDateTime createdAtWithTime = randomizeTimeOfDay(request.getCreatedAt());

        Transaction transaction = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(request.getAmount())
                .note("Fake data for chart")
                .createdAt(createdAtWithTime)
                .build();

        return transactionRepository.save(transaction);
    }

    private LocalDateTime randomizeTimeOfDay(LocalDate date) {
        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        int second = random.nextInt(60);
        return date.atTime(hour, minute, second);
    }

}

