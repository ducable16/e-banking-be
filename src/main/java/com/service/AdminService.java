package com.service;

import com.entity.Transaction;
import com.entity.User;
import com.enums.AccountStatus;
import com.enums.Role;
import com.exception.EntityNotFoundException;
import com.exception.WrongOtpException;
import com.repository.TransactionRepository;
import com.repository.UserRepository;
import com.request.*;
import com.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.request.AdminTransferFilterRequest.KeywordType.EMAIL;
import static com.request.AdminTransferFilterRequest.SearchType.*;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public void lockUser(Integer userId) {
        updateUserStatus(userId, AccountStatus.LOCKED);
    }

    public UserResponse addUser(AddUserRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .account(authService.generateUniqueAccountNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .balance(0L)
                .createdAt(LocalDateTime.now())
                .status(request.getStatus() != null ? request.getStatus() : AccountStatus.ACTIVE)
                .role(request.getRole() != null ? request.getRole() : Role.CUSTOMER)
                .build();
        userRepository.save(user);
        return new UserResponse(user);
    }

    public void unlockUser(Integer userId) {
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

    public List<Transaction> filterTransactions(AdminTransferFilterRequest request) {
        // Chuyển LocalDate → LocalDateTime
        LocalDateTime start = request.getStartDate() != null
                ? request.getStartDate().atStartOfDay()
                : LocalDateTime.MIN;
        LocalDateTime end = request.getEndDate() != null
                ? request.getEndDate().atTime(LocalTime.MAX)
                : LocalDateTime.MAX;

        String keyword = request.getKeyword() != null ? request.getKeyword().trim() : "";
        AdminTransferFilterRequest.SearchType searchType = request.getSearchType();
        AdminTransferFilterRequest.KeywordType keywordType = request.getKeywordType();

        // Nếu không có keyword → lấy toàn bộ trong khoảng thời gian
        if (keyword.isBlank()) {
            return transactionRepository.findAllByCreatedAtBetween(start, end);
        }

        switch (searchType) {
            case ALL:
                if (keywordType == AdminTransferFilterRequest.KeywordType.EMAIL) {
                    return transactionRepository.findBySenderOrReceiverEmailContainingAndCreatedAtBetween(
                            keyword, start, end
                    );
                } else {
                    return transactionRepository.findBySenderOrReceiverAccountContainingAndCreatedAtBetween(
                            keyword, start, end
                    );
                }

            case SENDER:
                if (keywordType == AdminTransferFilterRequest.KeywordType.EMAIL) {
                    return transactionRepository.findBySender_EmailContainingIgnoreCaseAndCreatedAtBetween(
                            keyword, start, end
                    );
                } else {
                    return transactionRepository.findBySender_AccountContainingAndCreatedAtBetween(
                            keyword, start, end
                    );
                }

            case RECEIVER:
                if (keywordType == AdminTransferFilterRequest.KeywordType.EMAIL) {
                    return transactionRepository.findByReceiver_EmailContainingIgnoreCaseAndCreatedAtBetween(
                            keyword, start, end
                    );
                } else {
                    return transactionRepository.findByReceiver_AccountContainingAndCreatedAtBetween(
                            keyword, start, end
                    );
                }

            default:
                return List.of(); // fallback nếu không khớp
        }
    }
}
