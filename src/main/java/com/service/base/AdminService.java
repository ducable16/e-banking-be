package com.service.base;

import com.model.Transaction;
import com.model.User;
import com.request.AccountStatusRequest;
import com.request.TopUpRequest;

import java.util.List;

public interface AdminService {

    List<User> getUsers();

    User getUserById(Integer userId);

    void blockUser(Integer userId);

    void unblockUser(Integer userId);

    Boolean deleteUser(Integer userId);

    void changeUserAccountStatus(AccountStatusRequest request);

    List<Transaction> getTransactions();

    Transaction getTransactionById(Integer transactionId);

    Boolean topUpBalance(TopUpRequest request);
}
