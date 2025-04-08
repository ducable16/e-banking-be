package com.service.impl;

import com.entity.UserSession;
import com.entity.Transaction;
import com.entity.User;
import com.enums.AccountStatus;
import com.exception.EntityNotFoundException;
import com.repository.LoginSessionRepository;
import com.repository.TransactionRepository;
import com.repository.UserRepository;
import com.request.AccountStatusRequest;
import com.request.TopUpRequest;
import com.service.base.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final LoginSessionRepository loginSessionRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException());
    }

    @Override
    public void blockUser(Integer userId) {
        updateUserStatus(userId, AccountStatus.BLOCKED);
    }

    @Override
    public void unblockUser(Integer userId) {
        updateUserStatus(userId, AccountStatus.ACTIVE);
    }

    @Override
    public Boolean deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) return false;
        userRepository.deleteById(userId);
        return true;
    }

    @Override
    public void changeUserAccountStatus(AccountStatusRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException());
        user.setStatus(request.getStatus());
        userRepository.save(user);
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(Integer transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException());
    }

    @Override
    public List<UserSession> getUserSessions() {
        return loginSessionRepository.findAll();
    }

    @Override
    public UserSession getUserSessionById(Integer userId) {
        return loginSessionRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException());
    }

    @Override
    public Boolean topUpBalance(TopUpRequest request) {
        User user = userRepository.findByUserId(request.getUserId()).orElseThrow(() -> new EntityNotFoundException());
        Long currentBalance = user.getBalance() != null ? user.getBalance() : 0L;
        user.setBalance(currentBalance + request.getAmount());
        userRepository.save(user);
        return true;
    }

    // Helper method
    private Boolean updateUserStatus(Integer userId, AccountStatus status) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException());
        user.setStatus(status);
        userRepository.save(user);
        return true;
    }
}
