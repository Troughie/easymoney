package com.example.MoneyLover.infra.Bills.Mapper;

import com.example.MoneyLover.infra.Bills.DT0.BillRecurring;
import com.example.MoneyLover.infra.Bills.Entity.Bill;
import com.example.MoneyLover.infra.Recurring.Entity.Recurring;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillMapper {

    BillMapper INSTANCE = Mappers.getMapper(BillMapper.class);

    @Mapping(target = "frequency" ,source = "frequency")
    Recurring toBillRecurring(BillRecurring BillRecurring);
    @Mapping(target = "wallet", source = "wallet", ignore = true)
    @Mapping(target = "category", source = "category", ignore = true)
    @Mapping(source = "amount",target = "amount")
    Bill toBill(BillRecurring BillRecurring);

}
