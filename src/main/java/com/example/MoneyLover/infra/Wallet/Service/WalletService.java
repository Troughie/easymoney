package com.example.MoneyLover.infra.Wallet.Service;

import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.Wallet.Dto.Wallet_dto;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import com.example.MoneyLover.shares.Entity.PaginationParams;

public interface WalletService {
    ApiResponse<?> addWallet(User user, Wallet_dto walletDto);
    ApiResponse<?> allWallet(User user);
    ApiResponse<?> wallets(User user, PaginationParams paginationParams);

    ApiResponse<?> changeMainWallet(String id,User user);

    ApiResponse<?> deleteWallet(String id);
}
