package com.example.MoneyLover.infra.Budget.Service;

import com.example.MoneyLover.infra.Budget.Dto.Budget_Dto;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface BudgetService {

    ApiResponse<?> getBudget(User user, String wallet,String type);

    ApiResponse<?> saveBudget(User user , Budget_Dto budgetDto);
    ApiResponse<?> deleteBudget(String id);
}
