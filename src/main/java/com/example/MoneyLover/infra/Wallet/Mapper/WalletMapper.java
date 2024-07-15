package com.example.MoneyLover.infra.Wallet.Mapper;

import com.example.MoneyLover.infra.Wallet.Dto.Wallet_dto;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WalletMapper {
    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    @Mapping(source = "name", target = "name")
    Wallet toWallet(Wallet_dto walletDto);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "id", target = "id")
    Wallet_dto toWallet_DTO(Wallet wallet);
}
