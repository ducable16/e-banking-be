package com.service;

import com.entity.Transaction;
import com.entity.User;
import com.repository.TransactionRepository;
import com.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

    public Transaction createFakeBill() {
        List<User> users = userRepository.findAll();
        if (users.size() < 2) {
            throw new IllegalStateException("Cần ít nhất 2 người dùng để tạo giao dịch giả.");
        }

        // chọn ngẫu nhiên sender và receiver (khác nhau)
        User sender = users.get(random.nextInt(users.size()));
        User receiver;
        do {
            receiver = users.get(random.nextInt(users.size()));
        } while (receiver.getUserId().equals(sender.getUserId()));

        long amount = (random.nextInt(1000) + 1) * 1000L; // 1,000 ~ 1,000,000
        String[] notes = {"Chuyển khoản", "Thanh toán hóa đơn", "Trả nợ", "Chuyển tiền bạn bè", "Giao dịch nội bộ"};

        Transaction transaction = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .note(notes[random.nextInt(notes.length)])
                .createdAt(LocalDateTime.now().minusDays(random.nextInt(150))) // để phục vụ biểu đồ theo tháng
                .build();

        return transactionRepository.save(transaction);
    }
}

