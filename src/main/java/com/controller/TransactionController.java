package com.controller;


import com.service.JwtService;
import com.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getMonthlyTransactionSummary(
            @RequestParam("userId") Integer userId,
            @RequestParam("month") int month,
            @RequestParam("year") int year) {

        Map<String, Long> summary = transactionService.getMonthlySummary(userId, month, year);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/last-5-months")
    public ResponseEntity<List<Map<String, Object>>> getLast5MonthsSummary(@RequestHeader("Authorization") String token) {
        Integer userId = JwtService.extractUserId(token);
        System.out.println(userId);
        List<Map<String, Object>> summary = transactionService.getLast5MonthsSummary(userId);
        return ResponseEntity.ok(summary);
    }
}
