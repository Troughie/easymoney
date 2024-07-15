package com.example.MoneyLover.infra.Budget.Mapper;

import com.example.MoneyLover.infra.Budget.Dto.Budget_Dto;
import com.example.MoneyLover.infra.Budget.Entity.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BudgetMapper {
    BudgetMapper INSTANCE = Mappers.getMapper(BudgetMapper.class);

    @Mapping(source = "wallet",target = "wallet",ignore = true)
    @Mapping(source = "category",target = "category",ignore = true)
    @Mapping(source = "id",target = "id",ignore = true)
    Budget toBudget(Budget_Dto budgetDto);
}
