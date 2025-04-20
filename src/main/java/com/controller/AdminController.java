package com.controller;

import com.entity.Transaction;
import com.entity.User;
import com.request.AccountStatusRequest;
import com.request.FakeBillRequest;
import com.request.TopUpRequest;
import com.request.TransactionFilterRequest;
import com.service.AdminService;
import com.service.TransactionService;
import com.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final UserService userService;

    private final TransactionService transactionService;

    @PostMapping("/status")
    public void changeUserAccountStatus(@RequestBody AccountStatusRequest request) {
        adminService.changeUserAccountStatus(request);
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(adminService.getUsers());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Integer userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }

    @PutMapping("/users/{userId}/block")
    public void blockUser(@PathVariable Integer userId) {
        adminService.blockUser(userId);
    }

    @PutMapping("/users/{userId}/unblock")
    public void unblockUser(@PathVariable Integer userId) {
        adminService.unblockUser(userId);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(adminService.deleteUser(userId));
    }

    @GetMapping("/all-transactions")
    public ResponseEntity<List<Transaction>> getTransactions() {
        return ResponseEntity.ok(adminService.getTransactions());
    }

    @GetMapping("/transactions/{transactionId}")
    public Transaction getTransactionById(@PathVariable Integer transactionId) {
        return adminService.getTransactionById(transactionId);
    }
    @PostMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@RequestBody TransactionFilterRequest request) {
        return ResponseEntity.ok(userService.getTransactionHistory(request));
    }

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getTransactionsByUserId(userId));
    }

    @PostMapping("/users/topup")
    public ResponseEntity<?> topUpUser(@RequestBody TopUpRequest request) {
        return ResponseEntity.ok(adminService.topUpBalance(request));
    }
    @PostMapping("/bill")
    public ResponseEntity<Transaction> createFakeBill(@RequestBody FakeBillRequest request) {
        Transaction transaction = transactionService.createFakeTransaction(request);
        return ResponseEntity.ok(transaction);
    }

}
