package com.example.MoneyLover.infra.Wallet.ServiceImpl;

import com.example.MoneyLover.config.Redis.RedisService;
import com.example.MoneyLover.infra.Budget.Entity.Budget;
import com.example.MoneyLover.infra.Budget.Repository.BudgetRepo;
import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.Category.Entity.CategoryType;
import com.example.MoneyLover.infra.Category.Repository.CategoryRepo;
import com.example.MoneyLover.infra.Transaction.Entity.Transaction;
import com.example.MoneyLover.infra.Transaction.Repository.TransactionRepo;
import com.example.MoneyLover.infra.User.Dto.UserResponse;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.User.Mapper.UserMapper;
import com.example.MoneyLover.infra.User.Repository.UserRepository;
import com.example.MoneyLover.infra.Wallet.Dto.ManagerResponse;
import com.example.MoneyLover.infra.Wallet.Dto.WalletManager;
import com.example.MoneyLover.infra.Wallet.Dto.Wallet_dto;
import com.example.MoneyLover.infra.Wallet.Entity.Manager;
import com.example.MoneyLover.infra.Wallet.Entity.Permission;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
import com.example.MoneyLover.infra.Wallet.Entity.WalletType;
import com.example.MoneyLover.infra.Wallet.Mapper.WalletMapper;
import com.example.MoneyLover.infra.Wallet.Repository.ManagerRepo;
import com.example.MoneyLover.infra.Wallet.Repository.WalletRepo;
import com.example.MoneyLover.infra.Wallet.Service.WalletService;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import com.example.MoneyLover.shares.Entity.Pagination;
import com.example.MoneyLover.shares.Entity.PaginationParams;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import com.example.MoneyLover.shares.Service.ServiceExtended;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    private final ManagerRepo managerRepo;
    private final BudgetRepo budgetRepo;
    private final UserRepository userRepository;

    private final TransactionRepo transactionRepo;

    private final CategoryRepo categoryRepo;

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

    public ApiResponse<?> addManager(User user,WalletManager walletManager){
        Wallet wallet = walletRepo.findWalletById(walletManager.getWalletId());
        User user1 =userRepository.findTopById(walletManager.getUserId());
        if(user1==null){
            return _res.createErrorResponse("User not found",404);
        }
        if(wallet==null){
            return _res.createErrorResponse("Wallet not found",404);
        }
        Manager managerFound = wallet.getManagers().stream().filter(m->m.getUser().getId().equals(user.getId())).findFirst().orElse(null);

        if(!wallet.getUser().getId().equals(user.getId()) || (managerFound!=null && !managerFound.getPermission().equals(Permission.All)) ){
            return _res.createErrorResponse("Can't add manager, you don't have permission!!!",400);
        }

        if(wallet.getUser().getId().equals(walletManager.getUserId())){
            return _res.createErrorResponse("Can't add manager to yourself",400);
        }

        if(wallet.getManagers().stream().anyMatch(m->m.getUser().getId().equals(walletManager.getUserId()))){
            return _res.createErrorResponse("Manager already exists",400);
        }

        Manager manager = new Manager();
        manager.setUser(user1);
        manager.setWallet(wallet);
        manager.setPermission(Permission.Read);
        wallet.getManagers().add(manager);
        walletRepo.save(wallet);
        return _res.createSuccessResponse("Add manger successfully",200,manager);
    }

    public ApiResponse<?> removeManager(User user, WalletManager walletManager) {
        // Reuse the private method to get the wallet and manager
        Object[] walletAndManager = getWalletAndManager(user, walletManager);
        Wallet wallet = (Wallet) walletAndManager[0];
        Manager manager = (Manager) walletAndManager[1];

        wallet.getManagers().remove(manager);
        List<Transaction> transactions =wallet.getTransactions().stream().filter(t->t.getUser().getId().equals(manager.getUser().getId())).toList();
        List<Budget> budgets = wallet.getBudgets().stream().filter(b->b.getUser().getId().equals(manager.getUser().getId())).toList();
        wallet.getBudgets().removeAll(budgets);
        wallet.getTransactions().removeAll(transactions);
        transactionRepo.deleteAll(transactions);
        budgetRepo.deleteAll(budgets);

        walletRepo.save(wallet);
        managerRepo.deleteById(manager.getId());
        return _res.createSuccessResponse("Success", 200);
    }

    public ApiResponse<?> changePermission(User user, WalletManager walletManager) {
        // Reuse the private method to get the wallet and manager
        Object[] walletAndManager = getWalletAndManager(user, walletManager);
        Wallet wallet = (Wallet) walletAndManager[0];
        Manager manager = (Manager) walletAndManager[1];


        manager.setPermission(Permission.valueOf(walletManager.getPermission()));
        walletRepo.save(wallet);
        return _res.createSuccessResponse("Success", 200);
    }

    // Private method to encapsulate common logic
    private Object[] getWalletAndManager(User user, WalletManager walletManager) {
        Wallet wallet = walletRepo.findWalletById(walletManager.getWalletId());
        if (wallet == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found");
        }

        Manager manager = wallet.getManagers().stream()
                .filter(m -> m.getUser().getId().equals(walletManager.getUserId()))
                .findFirst()
                .orElse(null);

        Manager manager2 = wallet.getManagers().stream()
                .filter(m -> m.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElse(null);

        if ( manager2 != null && !manager2.getPermission().equals(Permission.All)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Failure, you don't have permission!!!");
        }
        return new Object[] { wallet, manager };
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
            List<Wallet> wallets = walletRepo.findWalletsAll(user);

            List<Wallet_dto> walletDtos=new ArrayList<>();

            for (Wallet wallet : wallets) {
                Wallet_dto wallet_dto = calculatorBalance(wallet);
                walletDtos.add(wallet_dto);
            }

        return _res.createSuccessResponse("Successfully",200,walletDtos);
    }

    private Wallet_dto calculatorBalance(Wallet wallet)
    {
        List<Transaction> transExpense = getTransactionsByTypeAndDate(wallet,CategoryType.Expense,null);

        List<Transaction> transIncome = getTransactionsByTypeAndDate(wallet,CategoryType.Income,null);

        long balancePlus = calculateTotalAmount(transExpense);
        long balanceDivide = calculateTotalAmount(transIncome);
        Wallet_dto resultWallet = WalletMapper.INSTANCE.convertCustom(wallet);
        resultWallet.setBalance(wallet.getBalance()-balancePlus+balanceDivide);
        return resultWallet;
    }

    public ApiResponse<?> deleteWallet(User user,String id)
    {
        try {
            Wallet wallet = walletRepo.findWalletById(id);

            if(wallet==null){
                return _res.createErrorResponse("Wallet not found",404);
            }

            if(!wallet.getUser().getId().equals(user.getId())){
                return _res.createErrorResponse("Can't delete wallet, you don't have permission!!!",400);
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

    public ApiResponse<?> wallets(User user, PaginationParams paginationParams) {

        Sort sort = paginationParams.getSort().equalsIgnoreCase("asc")?Sort.by(paginationParams.getField()).ascending()
                                                                                    :Sort.by(paginationParams.getField()).descending();
        Pageable pageable =  PageRequest.of(paginationParams.getPageNumber(),paginationParams.getDocumentsPerPage(),sort);

        Page<Wallet> walletsPage =walletRepo.findWalletsAll(user,pageable);

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
