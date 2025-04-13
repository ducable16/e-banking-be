package com.service;

import com.entity.Transaction;
import com.entity.User;
import com.exception.EntityNotFoundException;
import com.exception.IllegalArgumentException;
import com.repository.TransactionRepository;
import com.repository.UserRepository;
import com.request.ChangePasswordRequest;
import com.request.TransactionFilterRequest;
import com.request.TransferRequest;
import com.request.UserProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public Integer extractUserId(String token) {
        return JwtService.extractUserId(token);
    }

    public User getProfile(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public Boolean changePassword(ChangePasswordRequest request) {
        User user = getProfile(request.getUserId());
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) return false;

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    public Boolean updateProfile(UserProfileUpdateRequest request) {
        User user = getProfile(request.getUserId());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        userRepository.save(user);
        return true;
    }

    @Transactional
    public Boolean transferMoney(TransferRequest request) {
        Long amount = request.getAmount();
        if (request.getAmount() <= 0) throw new IllegalArgumentException("Amount must be greater than 0");

        User sender = getProfile(request.getSenderId());
        User receiver = getProfile(request.getReceiverId());

        if (sender.getBalance() < amount) return false;

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        userRepository.saveAll(List.of(sender, receiver));

        Transaction tx = new Transaction();
        tx.setSender(sender);
        tx.setReceiver(receiver);
        tx.setAmount(amount);
        tx.setNote(request.getNote());
        tx.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(tx);
        return true;
    }

    public List<Transaction> getTransactionHistory(TransactionFilterRequest request) {
        LocalDateTime start = (request.getStartDate() != null) ? request.getStartDate().atStartOfDay() : LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = (request.getEndDate() != null) ? request.getEndDate().plusDays(1).atStartOfDay() : LocalDateTime.of(2030, 1, 1, 0, 0);

        return transactionRepository.findAll().stream()
                .filter(tx ->
                        (tx.getSender().getUserId().equals(request.getUserId()) || tx.getReceiver().getUserId().equals(request.getUserId())) &&
                                (tx.getCreatedAt().isAfter(start) && tx.getCreatedAt().isBefore(end))
                )
                .sorted(Comparator.comparing(Transaction::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public Long getBalance(Integer userId) {
        return getProfile(userId).getBalance();
    }

    public List<Transaction> getTransactionsByUserId(Integer userId) {
        return transactionRepository.findByUserInvolved(userId);
    }
}
