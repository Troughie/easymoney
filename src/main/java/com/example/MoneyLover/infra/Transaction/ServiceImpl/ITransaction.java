package com.example.MoneyLover.infra.Transaction.ServiceImpl;

import com.example.MoneyLover.config.Redis.RedisService;
import com.example.MoneyLover.infra.Budget.Entity.Budget;
import com.example.MoneyLover.infra.Budget.Repository.BudgetRepo;
import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.Category.Entity.CategoryType;
import com.example.MoneyLover.infra.Category.Entity.Debt_loan_type;
import com.example.MoneyLover.infra.Category.Repository.CategoryRepo;
import com.example.MoneyLover.infra.Notification.Entiti.TypeNotification;
import com.example.MoneyLover.infra.Notification.Service.NotificationService;
import com.example.MoneyLover.infra.Recurring.Entity.Recurring;
import com.example.MoneyLover.infra.Recurring.Repository.RecurringRepo;
import com.example.MoneyLover.infra.Transaction.Dto.*;
import com.example.MoneyLover.infra.Transaction.Entity.Transaction;
import com.example.MoneyLover.infra.Transaction.Mapper.TransactionMapper;
import com.example.MoneyLover.infra.Transaction.Repository.TransactionRepo;
import com.example.MoneyLover.infra.Transaction.Service.TransactionService;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.Wallet.Entity.Manager;
import com.example.MoneyLover.infra.Wallet.Entity.Permission;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ITransaction extends ServiceExtended implements TransactionService {
    private final ResponseException _res;

    private final TransactionRepo transactionRepo;
    private final NotificationService notificationService;
    private final CategoryRepo categoryRepo;
    private final WalletRepo walletRepo;
    private final RecurringRepo recurringRepo;
    private final BudgetRepo budgetRepo;

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
        }else{
            filterTransaction.setEnd(today);
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
            Wallet wallet =walletRepo.findWalletById(transactionDtoAdd.getWallet());
            if(wallet==null){
                return _res.createErrorResponse("Wallet not found",404);
            }
            List<User> users = new java.util.ArrayList<>(wallet.getManagers().stream().map(Manager::getUser).toList());
            boolean isPermission=isPermission(wallet,user,Permission.Write);
            if(isPermission){
                    return _res.createErrorResponse("Can't add transaction, you don't have permission!!!",400);
            }

            if(!wallet.getUser().getId().equals(user.getId())){
                users.add(wallet.getUser());
                users.remove(user);
            }
            Transaction transaction=mappedTransaction(user,transactionDtoAdd,null);
            transactionRepo.save(transaction);

            Budget budget = budgetRepo.findBudgetAmount(user.getId(),transactionDtoAdd.getWallet(),transactionDtoAdd.getCategory(),LocalDate.now());

            if(budget!=null){
                List<Transaction> transactions = transactionRepo.getTransactionByDateBetweenAndCategoryId(budget.getPeriod_start(),budget.getPeriod_end(),transactionDtoAdd.getCategory());
                List<Transaction> tranExpense = transactions.stream().filter(e->e.getCategory().getCategoryType().equals(CategoryType.Expense)).toList();
                List<Transaction> tranIncome = transactions.stream().filter(e->e.getCategory().getCategoryType().equals(CategoryType.Income)).toList();

                long totalAmount = calculateTotalAmount(tranIncome) - calculateTotalAmount(tranExpense);
                if(budget.getAmount()<Math.abs(totalAmount)){
                    List<User> users1 = new ArrayList<>();
                    users1.add(user);

                    if(!user.getId().equals(wallet.getUser().getId())){
                        users1.add(wallet.getUser());
                    }
                    long amount = budget.getAmount()-totalAmount;
                    String message = " spent over " + amount + " in budget ";
                    notificationService.sendNotificationBudget(users1,user.getUsername(), message, budget.getCategory().getName(), TypeNotification.budget,wallet.getId());
                }
            }
            notificationService.sendNotificationTransaction(users,user.getUsername(),wallet.getName(),transaction.getCategory().getName());

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



    private ApiResponse<?> specFilter(User user, Filter_transaction filterTransaction) {
        Wallet wallet = walletRepo.findWalletById(filterTransaction.getWallet());

        Specification<Transaction> spec = SpecificationDynamic.byFilter(filterTransaction,user.getId(),false,wallet.getManagers());
        List<Transaction> transactionPage = transactionRepo.findAll(spec);

        List<Transaction_Response> transactions = transactionPage.stream().map(TransactionMapper.INSTANCE::toTransactionDto).toList();
        return _res.createSuccessResponse(200,transactions);
    }

//    private long calculateTotalAmount(List<Transaction> transactions) {
//        long amount =0;
//        for (Transaction transaction : transactions) {
//            if(transaction.getCategory().getCategoryType().equals(CategoryType.Expense)){
//                amount -= transaction.getAmount();
//            }else{
//                amount += transaction.getAmount();
//            }
//        }
//    }

    private long tranBalanceMonth(LocalDate date, Wallet wallet) {

        List<Transaction> transExpenseEnding = getTransactionsByTypeAndDate(wallet, CategoryType.Expense,date);

        List<Transaction> transIncomeEnding = getTransactionsByTypeAndDate(wallet,CategoryType.Income,date);

        List<Transaction> transDebtLoan = getTransactionsByTypeAndDate(wallet,CategoryType.Debt_Loan,date);

        List<Transaction> tranDebt =transDebtLoan.stream().filter(t->t.getCategory().getDebt_loan_type()== Debt_loan_type.Debt.getValue()).toList();
        List<Transaction> transLoan =transDebtLoan.stream().filter(t->t.getCategory().getDebt_loan_type()==Debt_loan_type.Loan.getValue()).toList();


        long balancePlusEnding = calculateTotalAmount(transExpenseEnding);
        long balanceDivideEnding = calculateTotalAmount(transIncomeEnding);
        long balancePlusDebt =calculateTotalAmount(tranDebt);
        long balanceDivideDebt =calculateTotalAmount(transLoan);
        return wallet.getBalance() -balancePlusEnding +balanceDivideEnding+balancePlusDebt-balanceDivideDebt;
    }



    public ApiResponse<?> deleteTransaction(String id,User user,String walletId)
    {
        try {
            Wallet wallet =walletRepo.findWalletById(walletId);
            boolean isPermission=isPermission(wallet,user,Permission.Delete);
            if(isPermission){
                return _res.createErrorResponse("Can't delete transaction, you don't have permission!!!",400);
            }
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
