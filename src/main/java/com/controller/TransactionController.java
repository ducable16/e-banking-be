package com.controller;


import com.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
