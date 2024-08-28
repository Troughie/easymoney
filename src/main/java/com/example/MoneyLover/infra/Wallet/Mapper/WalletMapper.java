package com.example.MoneyLover.infra.Wallet.Mapper;

import com.example.MoneyLover.infra.Transaction.Entity.Transaction;
import com.example.MoneyLover.infra.User.Mapper.UserMapper;
import com.example.MoneyLover.infra.Wallet.Dto.ManagerResponse;
import com.example.MoneyLover.infra.Wallet.Dto.Wallet_dto;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Mapper
public interface WalletMapper {
    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "managers", target = "managers")
    Wallet_dto toWallet_DTO(Wallet wallet);

    default Wallet_dto convertCustom(Wallet wallet) {
        List<ManagerResponse> managers = wallet.getManagers().stream().map(ManagerMapper.INSTANCE::toManagerResponse).toList();
        List<Transaction> transactions = wallet.getTransactions().stream()
                .filter(e -> managers.stream()
                        .anyMatch(manager -> manager.getUser().getId().equals(e.getUser().getId())) && e.getRecurring() == null)
                .toList();

        long totalTransaction = transactions.size();

        long totalAmount = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(0L, Long::sum);
        List<ManagerResponse> managerResponses = new ArrayList<>();
        for (ManagerResponse managerResponse : managers) {
            managerResponse.setTotalTransaction(totalTransaction);
            managerResponse.setTotalAmount(totalAmount);
            managerResponses.add(managerResponse);
        }
        Wallet_dto walletDto = WalletMapper.INSTANCE.toWallet_DTO(wallet);
        walletDto.setManagers(managerResponses);
        walletDto.setUser(UserMapper.INSTANCE.toUserResponse(wallet.getUser()));
        return walletDto;
    }

//    default List<Wallet_dto> convertCustomList(List<Wallet> wallets) {
//        return wallets.stream().map(this::convertCustom).toList();
//    }


    @Mapping(source = "name", target = "name")
    Wallet toWallet(Wallet_dto walletDto);

}
