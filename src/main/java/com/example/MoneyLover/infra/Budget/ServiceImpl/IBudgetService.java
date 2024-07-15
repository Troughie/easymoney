package com.example.MoneyLover.infra.Budget.ServiceImpl;

import com.example.MoneyLover.infra.Budget.Dto.Budget_Dto;
import com.example.MoneyLover.infra.Budget.Entity.Budget;
import com.example.MoneyLover.infra.Budget.Mapper.BudgetMapper;
import com.example.MoneyLover.infra.Budget.Repository.BudgetRepo;
import com.example.MoneyLover.infra.Budget.Service.BudgetService;
import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.Category.Repository.CategoryRepo;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
import com.example.MoneyLover.infra.Wallet.Repository.WalletRepo;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IBudgetService implements BudgetService {
    private final ResponseException _res;

    private final BudgetRepo budgetRepo;
    private final CategoryRepo categoryRepo;
    private final WalletRepo walletRepo;

    public ApiResponse<?> getBudget(User user, String wallet,String type)
    {
        LocalDate today = LocalDate.now();
        if(type==null||type.isEmpty()){
        return _res.createSuccessResponse(200,budgetRepo.findAllByUser(user,wallet,today));
        }
        return _res.createSuccessResponse(200,budgetRepo.findAllByUserExpired(user,wallet,today));
    }


    public ApiResponse<?> saveBudget(User user, Budget_Dto budgetDto) {
        try {
            Budget budget = BudgetMapper.INSTANCE.toBudget(budgetDto);
            Category category = categoryRepo.findById(budgetDto.getCategory())
                    .orElseThrow();
            Wallet wallet = walletRepo.findById(budgetDto.getWallet())
                    .orElseThrow();
            budget.setCategory(category);
            budget.setWallet(wallet);
            budget.setUser(user);

            if (budgetDto.getOverWrite() != null) {
                Optional<Budget> existingBudget = budgetRepo.findById(budgetDto.getId());

                if (existingBudget.isPresent()) {
                    Budget existingBudgetObj = existingBudget.get();
                    budgetRepo.deleteById(budgetDto.getId());

                    if (!budgetDto.getOverWrite()) {
                        budget.setAmount(existingBudgetObj.getAmount() + budgetDto.getAmount());
                    }
                } else {
                    return _res.createErrorResponse("Budget not found", 500);
                }
            }

            budgetRepo.save(budget);
            return _res.createSuccessResponse(200, budget);

        } catch (Exception e) {
            return _res.createErrorResponse(e.getMessage(), 500);
        }
    }

    public ApiResponse<?> deleteBudget(String id) {
        try {
            boolean exist = budgetRepo.existsById(id);
            if(!exist){
                return _res.createSuccessResponse("Budget not found!!", 400);
            }
            budgetRepo.deleteById(id);
            return _res.createSuccessResponse("Delete budget successfully", 200);
        } catch (Exception e) {
            return _res.createErrorResponse(e.getMessage(), 500);
        }
    }

}
