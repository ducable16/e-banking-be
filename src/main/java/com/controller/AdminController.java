package com.controller;

import com.entity.Transaction;
import com.entity.User;
import com.request.*;
import com.response.ApiResponse;
import com.service.AdminService;
import com.service.TransactionService;
import com.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.service.JwtService.validation;

@RestController
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Integer userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }
    @PutMapping("/user-profile")
    public ResponseEntity<Boolean> updateProfile(@RequestBody UserProfileUpdateRequest request) {
        return ResponseEntity.ok(userService.updateProfile(request));
    }

    @PostMapping("/add-user")
    public Object addUser(@RequestBody AddUserRequest request) {
        return adminService.addUser(request);
    }

    @PutMapping("/user/lock/{userId}")
    public void lockUser(@PathVariable Integer userId) {
        adminService.lockUser(userId);
    }

    @PutMapping("/user/unlock/{userId}")
    public void unlockUser(@PathVariable Integer userId) {
        adminService.unlockUser(userId);
    }

    @DeleteMapping("/user/{userId}")
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

    @PostMapping("/user/topup")
    public ResponseEntity<?> topUpUser(@RequestBody TopUpRequest request) {
        return ResponseEntity.ok(adminService.topUpBalance(request));
    }
    @PostMapping("/bill")
    public ResponseEntity<Transaction> createFakeBill(@RequestBody FakeBillRequest request) {
        Transaction transaction = transactionService.createFakeTransaction(request);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/transaction/filter")
    public ResponseEntity<?> filterTransactions(@RequestBody AdminTransferFilterRequest request) {
        List<Transaction> result = adminService.filterTransactions(request);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/dashboard-stats")
    public ResponseEntity<?> getDashboardStats() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getDashboardStats()));
    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody AdminChangePasswordRequest request) {
        return ResponseEntity.ok(adminService.adminChangePassword(request));
    }
}
