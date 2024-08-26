package com.example.MoneyLover.infra.Wallet.Mapper;

import com.example.MoneyLover.infra.User.Mapper.UserMapper;
import com.example.MoneyLover.infra.Wallet.Dto.ManagerResponse;
import com.example.MoneyLover.infra.Wallet.Entity.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ManagerMapper {
    ManagerMapper INSTANCE = Mappers.getMapper(ManagerMapper.class);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "permission", target = "permission")
    ManagerResponse toManagerResponse(Manager manager);

}
