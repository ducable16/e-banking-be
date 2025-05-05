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
import com.response.DashboardStatsResponse;
import com.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private final EmailService emailService;
    private final NotificationService notificationService;

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
        notificationService.balanceChangingNotification(user.getEmail(), request.getAmount(), user.getAccount(), "Nạp tiền", user.getBalance(), false);
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
        // Ngày mặc định an toàn với PostgreSQL
        LocalDateTime start = (request.getStartDate() != null)
                ? request.getStartDate().atStartOfDay()
                : LocalDateTime.of(2000, 1, 1, 0, 0);

        LocalDateTime end = (request.getEndDate() != null)
                ? request.getEndDate().atTime(LocalTime.MAX)
                : LocalDateTime.now();

        String keyword = request.getKeyword() != null ? request.getKeyword().trim() : "";
        AdminTransferFilterRequest.SearchType searchType = request.getSearchType();
        AdminTransferFilterRequest.KeywordType keywordType = request.getKeywordType();

        if (keyword.isBlank()) {
            return transactionRepository.findAllByCreatedAtBetween(start, end);
        }

        switch (searchType) {
            case ALL:
                return (keywordType == AdminTransferFilterRequest.KeywordType.EMAIL)
                        ? transactionRepository.findBySenderOrReceiverEmailContainingAndCreatedAtBetween(keyword, start, end)
                        : transactionRepository.findBySenderOrReceiverAccountContainingAndCreatedAtBetween(keyword, start, end);

            case SENDER:
                return (keywordType == AdminTransferFilterRequest.KeywordType.EMAIL)
                        ? transactionRepository.findBySender_EmailContainingIgnoreCaseAndCreatedAtBetween(keyword, start, end)
                        : transactionRepository.findBySender_AccountContainingAndCreatedAtBetween(keyword, start, end);

            case RECEIVER:
                return (keywordType == AdminTransferFilterRequest.KeywordType.EMAIL)
                        ? transactionRepository.findByReceiver_EmailContainingIgnoreCaseAndCreatedAtBetween(keyword, start, end)
                        : transactionRepository.findByReceiver_AccountContainingAndCreatedAtBetween(keyword, start, end);

            default:
                return List.of(); // fallback
        }
    }

    public DashboardStatsResponse getDashboardStats() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByStatus(AccountStatus.ACTIVE);
        long lockedUsers = userRepository.countByStatus(AccountStatus.LOCKED);

        long totalTransactions = transactionRepository.count();

        // Tính tổng số dư tài khoản
        Long totalBalance = userRepository.sumAllBalances();
        if (totalBalance == null) totalBalance = 0L;

        // Lấy 10 giao dịch gần nhất
        List<Transaction> recentTransactions = transactionRepository
                .findTop10ByOrderByCreatedAtDesc();

        List<DashboardStatsResponse.TransactionDto> transactionDtos = recentTransactions.stream()
                .map(tx -> DashboardStatsResponse.TransactionDto.builder()
                        .transactionId(tx.getTransactionId())
                        .amount(tx.getAmount())
                        .createdAt(tx.getCreatedAt())
                        .sender(new DashboardStatsResponse.UserDto(
                                tx.getSender().getFullName(),
                                tx.getSender().getAccount()))
                        .receiver(new DashboardStatsResponse.UserDto(
                                tx.getReceiver().getFullName(),
                                tx.getReceiver().getAccount()))
                        .build()
                ).toList();

        return DashboardStatsResponse.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .lockedUsers(lockedUsers)
                .totalTransactions(totalTransactions)
                .totalBalance(totalBalance)
                .recentTransactions(transactionDtos)
                .build();
    }

    public String adminChangePassword(AdminChangePasswordRequest request) {
        User user = userRepository.findByUserId(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return "Đổi mật khẩu user thành công";
    }


}
