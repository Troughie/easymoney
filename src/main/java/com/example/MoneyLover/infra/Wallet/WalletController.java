package com.example.MoneyLover.infra.Wallet;


import com.example.MoneyLover.infra.User.Dto.SignInDto;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.Wallet.Dto.WalletManager;
import com.example.MoneyLover.infra.Wallet.Dto.Wallet_dto;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
import com.example.MoneyLover.infra.Wallet.Entity.WalletType;
import com.example.MoneyLover.infra.Wallet.Service.WalletService;
import com.example.MoneyLover.shares.Constants.PaginationConstant;
import com.example.MoneyLover.shares.Entity.PaginationParams;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WalletController {
    private final ResponseException _res;
    private final WalletService walletService;

    @GetMapping("wallets")
    public ResponseEntity<?> index(@AuthenticationPrincipal User user,
                                 @ModelAttribute PaginationParams paginationParams
                                   )
    {
        var result =walletService.wallets(user,paginationParams);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("wallet/changeMain/{id}")
    public ResponseEntity<?> changeMainWallet(@AuthenticationPrincipal User user,@PathVariable String id)
    {
        var result =walletService.changeMainWallet(id,user);
        return _res.responseEntity(result,result.getCode());
    }



    @GetMapping("wallet/all")
    public ResponseEntity<?> all(@AuthenticationPrincipal User user)
    {
        var result =walletService.allWallet(user);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("addWallet")
    public ResponseEntity<?> addWallet (@AuthenticationPrincipal User user,@RequestBody Wallet_dto walletDto)
    {
        var result =walletService.addWallet(user,walletDto);
        return _res.responseEntity(result,result.getCode());
    }

    @DeleteMapping("wallet/delete/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user,@PathVariable String id)
    {
        var result =walletService.deleteWallet(user,id);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("manager/add")
    public ResponseEntity<?> addManager(@AuthenticationPrincipal User user,@RequestBody WalletManager manager)
    {
        var result =walletService.addManager(user,manager);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("manager/permission/change")
    public ResponseEntity<?> changePermission(@AuthenticationPrincipal User user,@RequestBody WalletManager manager)
    {
        var result =walletService.changePermission(user,manager);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("manager/delete")
    public ResponseEntity<?> deleteManager(@AuthenticationPrincipal User user,@RequestBody WalletManager manager)
    {
        var result =walletService.removeManager(user,manager);
        return _res.responseEntity(result,result.getCode());
    }
}
