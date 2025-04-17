package com.controller;

import com.entity.Transaction;
import com.entity.User;
import com.request.ChangePasswordRequest;
import com.request.TransactionFilterRequest;
import com.request.TransferRequest;
import com.request.UserProfileUpdateRequest;
import com.response.ApiResponse;
import com.service.JwtService;
import com.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.service.JwtService.validation;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        Integer userId = JwtService.extractUserId(token);
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(userService.changePassword(request));
    }

    @PutMapping("/profile")
    public ResponseEntity<Boolean> updateProfile(@RequestBody UserProfileUpdateRequest request, @RequestHeader("Authorization") String token) {
        validation(token, request.getUserId());
        return ResponseEntity.ok(userService.updateProfile(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Boolean> transferMoney(@RequestBody TransferRequest request, @RequestHeader("Authorization") String token) {
        User sender = userService.getProfileByAccount(request.getFromAccount());
        validation(token, sender.getUserId());
        return ResponseEntity.ok(userService.transferMoney(request));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@RequestBody TransactionFilterRequest request, @RequestHeader("Authorization") String token) {
        validation(token, request.getUserId());
        return ResponseEntity.ok(userService.getTransactionHistory(request));
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<Long> getBalance(@PathVariable Integer userId, @RequestHeader("Authorization") String token) {
        validation(token, userId);
        return ResponseEntity.ok(userService.getBalance(userId));
    }
    @GetMapping("/name")
    public ResponseEntity<?> getNameByAccount(@RequestParam("account") String accountNumber) {
        return ResponseEntity.ok(userService.getNameByAccount(accountNumber));
    }
}

