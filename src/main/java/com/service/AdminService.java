package com.service;

import com.entity.Transaction;
import com.entity.User;
import com.enums.AccountStatus;
import com.exception.EntityNotFoundException;
import com.repository.TransactionRepository;
import com.repository.UserRepository;
import com.request.AccountStatusRequest;
import com.request.TopUpRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public void blockUser(Integer userId) {
        updateUserStatus(userId, AccountStatus.BLOCKED);
    }

    public void unblockUser(Integer userId) {
        updateUserStatus(userId, AccountStatus.ACTIVE);
    }

    public Boolean deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) return false;
        userRepository.deleteById(userId);
        return true;
    }

    public void changeUserAccountStatus(AccountStatusRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setStatus(request.getStatus());
        userRepository.save(user);
    }

    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Integer transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
    }


    public Boolean topUpBalance(TopUpRequest request) {
        User user = userRepository.findByUserId(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Long currentBalance = user.getBalance() != null ? user.getBalance() : 0L;
        user.setBalance(currentBalance + request.getAmount());
        userRepository.save(user);
        return true;
    }

    // Helper method
    private Boolean updateUserStatus(Integer userId, AccountStatus status) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setStatus(status);
        userRepository.save(user);
        return true;
    }
}
