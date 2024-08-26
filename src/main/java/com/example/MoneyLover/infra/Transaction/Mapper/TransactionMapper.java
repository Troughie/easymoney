package com.example.MoneyLover.infra.Transaction.Mapper;

import com.example.MoneyLover.infra.Recurring.Entity.Recurring;
import com.example.MoneyLover.infra.Transaction.Dto.Transaction_Recurring;
import com.example.MoneyLover.infra.Transaction.Dto.Transaction_Response;
import com.example.MoneyLover.infra.Transaction.Dto.Transaction_dto_add;
import com.example.MoneyLover.infra.Transaction.Entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "amount",target = "amount")
    @Mapping(target = "wallet", source = "wallet", ignore = true)
    @Mapping(target = "category", source = "category", ignore = true)
    @Mapping(target = "remind", source = "remind", ignore = true)
    Transaction toTransaction (Transaction_dto_add transactionDtoAdd);

    @Mapping(target = "category", source = "category")
    @Mapping(target = "user", source = "user")
    Transaction_Response toTransactionDto(Transaction transaction);


    @Mapping(target = "wallet", source = "wallet", ignore = true)
    @Mapping(target = "category", source = "category", ignore = true)
    @Mapping(source = "amount",target = "amount")
    Transaction toTransactionRepeat(Transaction_Recurring transactionRecurring);


    @Mapping(target = "frequency" ,source = "frequency")
    Recurring toRecurring(Transaction_Recurring transactionRecurring);


}
