package com.service.base;

import com.model.Transaction;
import com.model.User;
import com.request.ChangePasswordRequest;
import com.request.TransactionFilterRequest;
import com.request.TransferRequest;
import com.request.UserProfileUpdateRequest;

import java.util.List;

public interface UserService {

    User getProfile(Integer userId);

    Boolean changePassword(ChangePasswordRequest request);

    Boolean updateProfile(UserProfileUpdateRequest request);

    Boolean transferMoney(TransferRequest request);

    List<Transaction> getTransactionHistory(TransactionFilterRequest request);

    Long getBalance(Integer userId);

    List<Transaction> getTransactionsByUserId(Integer userId);

}
