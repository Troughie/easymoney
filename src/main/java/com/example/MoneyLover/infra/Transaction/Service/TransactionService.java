package com.example.MoneyLover.infra.Transaction.Service;

import com.example.MoneyLover.infra.Transaction.Dto.Filter_transaction;
import com.example.MoneyLover.infra.Transaction.Dto.Transaction_Recurring;
import com.example.MoneyLover.infra.Transaction.Dto.Transaction_dto_add;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import com.example.MoneyLover.shares.Entity.PaginationParams;

import java.time.LocalDate;

public interface TransactionService {

    ApiResponse<?> addTransaction(User user , Transaction_dto_add transactionDtoAdd);

    ApiResponse<?> allTransaction(User user, Filter_transaction filterTransaction);


    ApiResponse<?> calculatorBalanceOpenNAnd(String wallet_id, LocalDate start, LocalDate end);

    ApiResponse<?> allTransactions(User user,Filter_transaction filterTransaction);

    ApiResponse<?> deleteTransaction(String id,User user,String walletId);
    ApiResponse<?> addTransactionRecurring(User user , Transaction_Recurring transactionRecurring);

    ApiResponse<?> allTransactionRecurring(User user);

    ApiResponse<?> addTransactionFromRecurring(String id,User user);
}
