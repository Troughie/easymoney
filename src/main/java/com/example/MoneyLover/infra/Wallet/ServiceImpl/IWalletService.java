package com.example.MoneyLover.infra.Wallet.ServiceImpl;

import com.example.MoneyLover.config.Redis.RedisService;
import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.Category.Entity.CategoryType;
import com.example.MoneyLover.infra.Category.Repository.CategoryRepo;
import com.example.MoneyLover.infra.Transaction.Entity.Transaction;
import com.example.MoneyLover.infra.Transaction.Repository.TransactionRepo;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.Wallet.Dto.Wallet_dto;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
import com.example.MoneyLover.infra.Wallet.Entity.WalletType;
import com.example.MoneyLover.infra.Wallet.Mapper.WalletMapper;
import com.example.MoneyLover.infra.Wallet.Repository.WalletRepo;
import com.example.MoneyLover.infra.Wallet.Service.WalletService;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import com.example.MoneyLover.shares.Entity.Pagination;
import com.example.MoneyLover.shares.Entity.PaginationParams;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import com.example.MoneyLover.shares.Service.ServiceExtended;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class IWalletService extends ServiceExtended implements WalletService  {
    private final ResponseException _res;

    private final WalletRepo walletRepo;

    private final TransactionRepo transactionRepo;

    private final CategoryRepo categoryRepo;

    private final RedisService _redis;
    @Override
    public ApiResponse<?> addWallet(User user, Wallet_dto walletDto) {
        Wallet wallet = WalletMapper.INSTANCE.toWallet(walletDto);
        List<Wallet> wallets =walletRepo.findAllByUser(user);
        if(wallets.isEmpty()){
            wallet.setMain(true);
        }
        boolean isGoalWallet=wallet.getType().equals(WalletType.GoalWallet.getName());
        wallet.setBalance(isGoalWallet?0:walletDto.getBalance());

        wallet.setUser(user);
        Wallet newWallet = walletRepo.save(wallet);
        if(isGoalWallet){
            Category category =categoryRepo.findCategoriesByDefaultIncomeTrue();
            Transaction transaction = new Transaction();
            transaction.setDate(LocalDate.now());
            transaction.setAmount(walletDto.getBalance());
            transaction.setWallet(newWallet);
            transaction.setUser(user);
            transaction.setCategory(category);
            transactionRepo.save(transaction);
        }
        return _res.createSuccessResponse("Add wallet successfully",200,newWallet);
    }

    public ApiResponse<?> changeMainWallet(String id,User user)
    {
        try {
            Wallet walletMain =walletRepo.findWalletIsMainTrue(user);
            walletMain.setMain(false);
            walletRepo.save(walletMain);
            Wallet wallet = walletRepo.findWalletById(id);
            wallet.setMain(!wallet.isMain());
            walletRepo.save(wallet);
            return _res.createSuccessResponse("Change main wallet successfully",200,wallet);
        }catch (Exception e){
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    @Override
    public ApiResponse<?> allWallet(User user) {
            List<Wallet> wallets = walletRepo.findAllByUser(user);
            List<Wallet_dto> walletDtos=new ArrayList<>();
            for (Wallet wallet:wallets) {
                Wallet_dto walletDto =calculatorBalance(wallet);
                walletDtos.add(walletDto);
            }

        return _res.createSuccessResponse("Successfully",200,walletDtos);
    }

    private Wallet_dto calculatorBalance(Wallet wallet)
    {
        LocalDate today = LocalDate.now();

        List<Transaction> transExpense = getTransactionsByTypeAndDate(wallet,CategoryType.Expense,today);

        List<Transaction> transIncome = getTransactionsByTypeAndDate(wallet,CategoryType.Income,today);


        long balancePlus = calculateTotalAmount(transExpense);
        long balanceDivide = calculateTotalAmount(transIncome);
        Wallet_dto resultWallet = WalletMapper.INSTANCE.toWallet_DTO(wallet);
        resultWallet.setBalance(wallet.getBalance()-balancePlus+balanceDivide);
        return resultWallet;
    }

    public void saveWalletToCache(User user,List<Wallet_dto> walletDtos)
    {
        _redis.setList("wallet"+user.getId(),walletDtos,30, TimeUnit.SECONDS);
    }


    public ApiResponse<?> deleteWallet(String id)
    {
        try {
            Wallet wallet = walletRepo.findWalletById(id);
            if(wallet==null){
                return _res.createErrorResponse("Wallet not found",404);
            }
            if(wallet.isMain()){
                return _res.createErrorResponse("Can't delete main wallet",400);
            }
            walletRepo.deleteById(id);
            return _res.createSuccessResponse("Delete wallet successfully",200);
        }catch (Exception e){
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public List<Wallet_dto> getWalletFromCache(User user)
    {
        return _redis.getList("wallet"+user.getId(),Wallet_dto.class);
    }

    public ApiResponse<?> wallets(User user, PaginationParams paginationParams) {

        Sort sort = paginationParams.getSort().equalsIgnoreCase("asc")?Sort.by(paginationParams.getField()).ascending()
                                                                                    :Sort.by(paginationParams.getField()).descending();
        Pageable pageable =  PageRequest.of(paginationParams.getPageNumber(),paginationParams.getDocumentsPerPage(),sort);

        Page<Wallet> walletsPage =walletRepo.findAllByUser(user,pageable);

        List<Wallet_dto> walletDtos = walletsPage.getContent().stream().map(this::calculatorBalance
        ).collect(Collectors.toList());

        return _res.createSuccessResponse(200,new Pagination<Wallet_dto>(
                walletDtos,
                walletsPage.getTotalPages(),
                paginationParams.getDocumentsPerPage(),
                paginationParams.getPageNumber(),
                walletsPage.getTotalElements(),
                walletsPage.isLast()
        ));
    }
}
