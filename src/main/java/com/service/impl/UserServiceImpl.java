package com.service.impl;

import com.model.Transaction;
import com.model.User;
import com.exception.EntityNotFoundException;
import com.exception.IllegalArgumentException;
import com.repository.TransactionRepository;
import com.repository.UserRepository;
import com.request.ChangePasswordRequest;
import com.request.TransactionFilterRequest;
import com.request.TransferRequest;
import com.request.UserProfileUpdateRequest;
import com.service.EmailService;
import com.service.JwtService;
import com.service.NotificationService;
import com.service.base.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.constant.Name.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final NotificationService notificationService;

    private final JwtService jwtService;

    @Override
    public User getProfile(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public Boolean changePassword(ChangePasswordRequest request) {
        User user = getProfile(request.getUserId());
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) return false;

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean updateProfile(UserProfileUpdateRequest request) {
        User user = getProfile(request.getUserId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        userRepository.save(user);
        return true;
    }

    @Override
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

        String senderNotification = notificationService.notificationFormat(request.getAmount(), sender.getAccount(), request.getNote(), sender.getBalance(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")), true);
        String receiverNotification = notificationService.notificationFormat(request.getAmount(), receiver.getAccount(), request.getNote(), receiver.getBalance(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")), true);
        emailService.sendEmail(sender.getEmail(), TRANSFER_NOTIFICATION_EMAIL_HEADER, senderNotification);
        emailService.sendEmail(receiver.getEmail(), TRANSFER_NOTIFICATION_EMAIL_HEADER, receiverNotification);
        return true;
    }

    @Override
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

    @Override
    public Long getBalance(Integer userId) {
        return getProfile(userId).getBalance();
    }

    @Override
    public List<Transaction> getTransactionsByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }
        return transactionRepository.findByUserInvolved(userId);
    }
}
