package com.example.MoneyLover.infra.Transaction;


import com.example.MoneyLover.infra.Transaction.Dto.Filter_transaction;
import com.example.MoneyLover.infra.Transaction.Dto.Transaction_Recurring;
import com.example.MoneyLover.infra.Transaction.Dto.Transaction_dto_add;
import com.example.MoneyLover.infra.Transaction.Repository.TransactionRepo;
import com.example.MoneyLover.infra.Transaction.Service.TransactionService;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.Entity.PaginationParams;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TransactionController {
    private final ResponseException _res;

    private final TransactionService transactionService;


    @GetMapping("transactions")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user, @ModelAttribute Filter_transaction filterTransaction)
    {
        var result =transactionService.allTransaction(user,filterTransaction);
        return _res.responseEntity(result,result.getCode());
    }

    @GetMapping("transactions/open")
    public ResponseEntity<?> gopen(@RequestParam("category")String category
    )
    {
        return _res.responseEntity(category,200);
    }
    @GetMapping("transactions/all")
    public ResponseEntity<?> getAll(@AuthenticationPrincipal User user,@ModelAttribute Filter_transaction  filterTransaction)
    {
        var result =transactionService.allTransactions(user,filterTransaction);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("transaction/add")
    public ResponseEntity<?> add(@AuthenticationPrincipal User user,@RequestBody Transaction_dto_add transactionDtoAdd)
    {
        var result =transactionService.addTransaction(user,transactionDtoAdd);
        return _res.responseEntity(result,result.getCode());
    }

    @GetMapping("transaction/balance")
    public ResponseEntity<?> getBalanceInMonth(@ModelAttribute Filter_transaction filterTransaction)
    {
        var result =transactionService.calculatorBalanceOpenNAnd(filterTransaction.getWallet(), filterTransaction.getStart(), filterTransaction.getEnd());
        return _res.responseEntity(result,result.getCode());
    }

    @DeleteMapping("transaction/delete/{id}")
    public ResponseEntity<?> deleteTransaction(@AuthenticationPrincipal User user, @PathVariable String id,@RequestBody String walletId)
    {
        walletId = walletId.replace("\"", "");
        var result =transactionService.deleteTransaction(id,user,walletId);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("transaction/recurring")
    public ResponseEntity<?> addTransactionRecurring(@AuthenticationPrincipal User user,@RequestBody Transaction_Recurring transactionRecurring)
    {
        var result =transactionService.addTransactionRecurring(user,transactionRecurring);
        return _res.responseEntity(result,result.getCode());
    }

    @GetMapping("transactions/recurring")
    public ResponseEntity<?> getTransactionRecurring(@AuthenticationPrincipal User user)
    {
        var result =transactionService.allTransactionRecurring(user);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("transactions/recurring/add/{id}")
    public ResponseEntity<?> deleteTransactionRecurring(@PathVariable String id,@AuthenticationPrincipal User user)
    {
        var result =transactionService.addTransactionFromRecurring(id,user);
        return _res.responseEntity(result,result.getCode());
    }
}
