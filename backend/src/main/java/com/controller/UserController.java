package com.controller;

import com.entity.Transaction;
import com.entity.User;
import com.request.ChangePasswordRequest;
import com.request.TransactionFilterRequest;
import com.request.TransferRequest;
import com.request.UserProfileUpdateRequest;
import com.service.base.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile/{userId}")
    public User getProfile(@PathVariable Integer userId) {
        return userService.getProfile(userId);
    }

    @PostMapping("/change-password")
    public Boolean changePassword(@RequestBody ChangePasswordRequest request) {
        return userService.changePassword(request);
    }

    @PutMapping("/profile")
    public Boolean updateProfile(@RequestBody UserProfileUpdateRequest request) {
        return userService.updateProfile(request);
    }

    @PostMapping("/transfer")
    public Boolean transferMoney(@RequestBody TransferRequest request) {
        return userService.transferMoney(request);
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactionHistory(@RequestBody TransactionFilterRequest request) {
        return userService.getTransactionHistory(request);
    }

    @GetMapping("/balance/{userId}")
    public Long getBalance(@PathVariable Integer userId) {
        return userService.getBalance(userId);
    }
}

