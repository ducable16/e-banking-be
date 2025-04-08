package com.controller;

import com.entity.Transaction;
import com.entity.User;
import com.entity.UserSession;
import com.request.AccountStatusRequest;
import com.request.TopUpRequest;
import com.service.base.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/status")
    public void changeUserAccountStatus(@RequestBody AccountStatusRequest request) {
        adminService.changeUserAccountStatus(request);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return adminService.getUsers();
    }

    @GetMapping("/users/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        return adminService.getUserById(userId);
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
    public Boolean deleteUser(@PathVariable Integer userId) {
        return adminService.deleteUser(userId);
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return adminService.getTransactions();
    }

    @GetMapping("/transactions/{transactionId}")
    public Transaction getTransactionById(@PathVariable Integer transactionId) {
        return adminService.getTransactionById(transactionId);
    }

    @GetMapping("/sessions")
    public List<UserSession> getLoginSessions() {
        return adminService.getUserSessions();
    }

    @GetMapping("/sessions/{userId}")
    public UserSession getLoginSessionById(@PathVariable Integer userId) {
        return adminService.getUserSessionById(userId);
    }

    @PostMapping("/users/topup")
    public Boolean topUpUser(@RequestBody TopUpRequest request) {
        return adminService.topUpBalance(request);
    }

}
