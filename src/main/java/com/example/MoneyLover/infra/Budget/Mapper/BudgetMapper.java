package com.example.MoneyLover.infra.Budget.Mapper;

import com.example.MoneyLover.infra.Budget.Dto.Budget_Dto;
import com.example.MoneyLover.infra.Budget.Dto.Budget_response;
import com.example.MoneyLover.infra.Budget.Entity.Budget;
import com.example.MoneyLover.infra.User.Mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface BudgetMapper {
    BudgetMapper INSTANCE = Mappers.getMapper(BudgetMapper.class);

    @Mapping(source = "wallet",target = "wallet",ignore = true)
    @Mapping(source = "category",target = "category",ignore = true)
    @Mapping(source = "id",target = "id",ignore = true)
    Budget toBudget(Budget_Dto budgetDto);

    @Mapping(source = "wallet",target = "wallet",ignore = true)
    @Mapping(source = "category",target = "category",ignore = true)
    Budget_response toBudgetResponse(Budget budget);

    default List<Budget_response> toBudgetResponseAll(List<Budget> budgets) {
        List<Budget_response> budgetResponses = new ArrayList<>();
        for (Budget b : budgets) {
            Budget_response budgetRes = toBudgetResponse(b);
            budgetRes.setCategory(b.getCategory());
            budgetRes.setWallet(b.getWallet().getId());
            budgetRes.setUser(UserMapper.INSTANCE.toUserResponse(b.getUser()));
            budgetResponses.add(budgetRes);
        }
        return budgetResponses;
    }

    default Budget_response toBudgetResponse2(Budget budget) {
        Budget_response budgetRes = toBudgetResponse(budget);
        budgetRes.setCategory(budget.getCategory());
        budgetRes.setWallet(budget.getWallet().getId());
        budgetRes.setUser(UserMapper.INSTANCE.toUserResponse(budget.getUser()));
        return budgetRes;
    }
}
