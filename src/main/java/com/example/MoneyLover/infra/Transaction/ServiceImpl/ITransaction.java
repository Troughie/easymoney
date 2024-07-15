package com.example.MoneyLover.infra.Transaction.ServiceImpl;

import com.example.MoneyLover.config.Redis.RedisService;
import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.Category.Entity.CategoryType;
import com.example.MoneyLover.infra.Category.Entity.Debt_loan_type;
import com.example.MoneyLover.infra.Category.Repository.CategoryRepo;
import com.example.MoneyLover.infra.Recurring.Entity.Recurring;
import com.example.MoneyLover.infra.Recurring.Repository.RecurringRepo;
import com.example.MoneyLover.infra.Transaction.Dto.*;
import com.example.MoneyLover.infra.Transaction.Entity.Transaction;
import com.example.MoneyLover.infra.Transaction.Mapper.TransactionMapper;
import com.example.MoneyLover.infra.Transaction.Repository.TransactionRepo;
import com.example.MoneyLover.infra.Transaction.Service.TransactionService;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
import com.example.MoneyLover.infra.Wallet.Repository.WalletRepo;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import com.example.MoneyLover.shares.Service.ServiceExtended;
import com.example.MoneyLover.utils.SpecificationDynamic;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ITransaction extends ServiceExtended implements TransactionService {
    private final ResponseException _res;

    private final TransactionRepo transactionRepo;
    private final CategoryRepo categoryRepo;
    private final WalletRepo walletRepo;
    private final RecurringRepo recurringRepo;

    private final RedisService _redis;


    /**
     * Retrieves all transactions for a user based on filter criteria.
     *
     * @param user The user for whom to retrieve transactions.
     * @param filterTransaction The filter criteria for transactions.
     * @return An ApiResponse containing the list of transactions.
     */
    public ApiResponse<?> allTransaction(User user, Filter_transaction filterTransaction) {
        // Get the current date
        LocalDate today = LocalDate.now();

        if(filterTransaction.getEnd()!=null)
        {
            boolean isDay = filterTransaction.getEnd().isAfter(today);
            filterTransaction.setEnd(isDay ? today : filterTransaction.getEnd());
        }

        // Generate a specification based on the filter criteria
        return specFilter(user, filterTransaction);
    }


    private Transaction mappedTransaction(User user,Transaction_dto_add transactionDtoAdd,Transaction_Recurring transactionRecurring) {
        Transaction transaction;
        Category category;
        Wallet wallet;
        if(transactionDtoAdd!=null){
            transaction = TransactionMapper.INSTANCE.toTransaction(transactionDtoAdd);
            category = categoryRepo.findCategoriesById(transactionDtoAdd.getCategory());
            wallet = walletRepo.findWalletById(transactionDtoAdd.getWallet());
        }else{
            transaction = TransactionMapper.INSTANCE.toTransactionRepeat(transactionRecurring);
            category = categoryRepo.findCategoriesById(transactionRecurring.getCategory());
            wallet = walletRepo.findWalletById(transactionRecurring.getWallet());
        }
        transaction.setCategory(category);
        transaction.setWallet(wallet);
        transaction.setUser(user);
        return transaction;
    }
    public ApiResponse<?> addTransaction(User user , Transaction_dto_add transactionDtoAdd){
        try {
            Transaction transaction=mappedTransaction(user,transactionDtoAdd,null);
            transactionRepo.save(transaction);
            _redis.removeValue("wallet"+user.getId());
            return _res.createSuccessResponse("Add transaction successfully",200,transaction);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> addTransactionRecurring(User user , Transaction_Recurring transactionRecurring){
        try {
            LocalDate date =calculateFromDateScheduled(transactionRecurring,null);
            Recurring recurring = TransactionMapper.INSTANCE.toRecurring(transactionRecurring);
            recurring.setFrom_date(date);
            Recurring rec = recurringRepo.save(recurring);
            Transaction transaction=mappedTransaction(user,null,transactionRecurring);
            transaction.setDate(date);
            transaction.setRemind(true);
            transaction.setRecurring(rec);
            transactionRepo.save(transaction);

            _redis.removeValue("wallet"+user.getId());
            return _res.createSuccessResponse("Add transaction successfully",200,recurring);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> calculatorBalanceOpenNAnd(String wallet_id,LocalDate start,LocalDate end){
        Wallet wallet = walletRepo.findWalletById(wallet_id);
        LocalDate today = LocalDate.now();

        long transOpen = tranBalanceMonth(start, wallet);

        long tranEnding;
        if(end!=null&&end.isAfter(today))
        {
            tranEnding = tranBalanceMonth(today, wallet);

        }else{
            tranEnding = tranBalanceMonth(end, wallet);
        }

        return _res.createSuccessResponse(200,new Balance(transOpen,tranEnding));

    }

    public ApiResponse<?> allTransactionRecurring(User user){

        List<Tran_Recurring_res> transRecurring = transactionRepo.getTranRecurring(user);
        return _res.createSuccessResponse(200,transRecurring);
    }

    @Override
    public ApiResponse<?> allTransactions(User user,Filter_transaction filterTransaction) {
        LocalDate now = LocalDate.now();
        filterTransaction.setEnd(now);
        return specFilter(user, filterTransaction);
    }

    private ApiResponse<?> specFilter(User user, Filter_transaction filterTransaction) {
        Specification<Transaction> spec = SpecificationDynamic.byFilter(filterTransaction,user.getId(),false);
        List<Transaction> transactionPage = transactionRepo.findAll(spec);

        List<Transaction_Response> transactions = transactionPage.stream().map(TransactionMapper.INSTANCE::toTransactionDto).toList();
        return _res.createSuccessResponse(200,transactions);
    }

    private long tranBalanceMonth(LocalDate date, Wallet wallet) {

        List<Transaction> transExpenseEnding = getTransactionsByTypeAndDate(wallet, CategoryType.Expense,date);

        List<Transaction> transIncomeEnding = getTransactionsByTypeAndDate(wallet,CategoryType.Income,date);

        List<Transaction> transDebtLoan = getTransactionsByTypeAndDate(wallet,CategoryType.Debt_Loan,date);

        List<Transaction> tranDebt =transDebtLoan.stream().filter(t->t.getCategory().getDebt_loan_type()== Debt_loan_type.Debt.getValue()).toList();
        List<Transaction> transLoan =transDebtLoan.stream().filter(t->t.getCategory().getDebt_loan_type()==Debt_loan_type.Loan.getValue()).toList();


        long balancePlusEnding = calculateTotalAmount(transExpenseEnding);
        long balanceDivideEnding =calculateTotalAmount(transIncomeEnding);
        long balancePlusDebt =calculateTotalAmount(tranDebt);
        long balanceDivideDebt =calculateTotalAmount(transLoan);
        return wallet.getBalance() -balancePlusEnding +balanceDivideEnding+balancePlusDebt-balanceDivideDebt;
    }

    public ApiResponse<?> deleteTransaction(String id,User user)
    {
        try {
            transactionRepo.deleteById(id);
            _redis.removeValue("wallet"+user.getId());
            return _res.createSuccessResponse("Delete transaction successfully",200);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> addTransactionFromRecurring(String id,User user){
        try {
            Transaction transaction = transactionRepo.findById(id).orElseThrow();
            Transaction newTran = new Transaction(transaction);
            newTran.setDate(LocalDate.now());
            newTran.setRemind(false);
            newTran.setRecurring(null);
            transactionRepo.save(newTran);
            _redis.removeValue("wallet"+user.getId());
            return _res.createSuccessResponse("Add successfully",200);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

}
