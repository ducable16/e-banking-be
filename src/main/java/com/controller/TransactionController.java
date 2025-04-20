package com.controller;


import com.enums.ErrorCode;
import com.request.ConfirmOtpRequest;
import com.request.SendOtpRequest;
import com.response.ApiResponse;
import com.service.JwtService;
import com.service.OtpService;
import com.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    private final OtpService otpService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getMonthlyTransactionSummary(
            @RequestParam("userId") Integer userId,
            @RequestParam("month") int month,
            @RequestParam("year") int year) {

        Map<String, Long> summary = transactionService.getMonthlySummary(userId, month, year);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/last-12-months")
    public ResponseEntity<List<Map<String, Object>>> getLast12MonthsSummary(@RequestHeader("Authorization") String token) {
        Integer userId = JwtService.extractUserId(token);
        List<Map<String, Object>> summary = transactionService.getLast12MonthsSummary(userId);
        return ResponseEntity.ok(summary);
    }
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(@RequestBody SendOtpRequest request) throws UnsupportedEncodingException {
        String otp = otpService.generateOtp(
                request.getEmail(),
                "Xác nhận giao dịch chuyển tiền"
        );
        return ResponseEntity.ok(ApiResponse.success("OTP đã được gửi tới email."));
    }

    @PostMapping("/confirm-otp")
    public ResponseEntity<ApiResponse<String>> confirmOtp(@RequestBody ConfirmOtpRequest request) {
        boolean valid = otpService.validateOtp(request.getEmail(), request.getOtp());

        if (!valid) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.INVALID_OTP, "Mã OTP không hợp lệ hoặc đã hết hạn."));
        }
        return ResponseEntity.ok(ApiResponse.success("Xác thực OTP thành công."));
    }
}
