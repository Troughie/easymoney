package com.example.MoneyLover.infra.Budget.ServiceImpl;

import com.example.MoneyLover.infra.Budget.Dto.Budget_Dto;
import com.example.MoneyLover.infra.Budget.Dto.Budget_response;
import com.example.MoneyLover.infra.Budget.Entity.Budget;
import com.example.MoneyLover.infra.Budget.Mapper.BudgetMapper;
import com.example.MoneyLover.infra.Budget.Repository.BudgetRepo;
import com.example.MoneyLover.infra.Budget.Service.BudgetService;
import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.Category.Repository.CategoryRepo;
import com.example.MoneyLover.infra.Notification.Entiti.TypeNotification;
import com.example.MoneyLover.infra.Notification.Repository.NotificationRepo;
import com.example.MoneyLover.infra.Notification.Service.NotificationService;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.Wallet.Entity.Manager;
import com.example.MoneyLover.infra.Wallet.Entity.Permission;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
import com.example.MoneyLover.infra.Wallet.Repository.WalletRepo;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import com.example.MoneyLover.shares.Service.ServiceExtended;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IBudgetService extends ServiceExtended implements BudgetService {
    private final ResponseException _res;

    private final BudgetRepo budgetRepo;
    private final CategoryRepo categoryRepo;
    private final NotificationService notificationService;
    private final WalletRepo walletRepo;

    public ApiResponse<?> getBudget(User user, String wallet,String type)
    {

        LocalDate today = LocalDate.now();
        if(type==null||type.isEmpty()){
            return _res.createSuccessResponse(200,BudgetMapper.INSTANCE.toBudgetResponseAll(budgetRepo.findAllByUser(wallet,today)));
        }
        return _res.createSuccessResponse(200,BudgetMapper.INSTANCE.toBudgetResponseAll(budgetRepo.findAllByUserExpired(wallet,today)));
    }


    public ApiResponse<?> saveBudget(User user, Budget_Dto budgetDto) {
        try {
            Wallet wallet = walletRepo.findById(budgetDto.getWallet())
                    .orElseThrow();
            Manager isManager = wallet.getManagers().stream().filter(m->m.getUser().getId().equals(user.getId())).findFirst().orElse(null);
            boolean isOwner =wallet.getUser().getId().equals(user.getId());

            if(isManager!=null && !(isManager.getPermission().equals(Permission.Write) || isManager.getPermission().equals(Permission.All))) {
                return _res.createErrorResponse("Can't add budget, you don't have permission!!!", 400);
            }

            Category category = categoryRepo.findById(budgetDto.getCategory())
                    .orElseThrow();
            // Create or update budget
            Budget budget;

            List<User> users = new java.util.ArrayList<>(wallet.getManagers().stream().map(Manager::getUser).toList());
            String message=null;
            if(!wallet.getUser().getId().equals(user.getId())){
                users.add(wallet.getUser());
                users.remove(user);
            }
            if (budgetDto.getId() == null) {
                budget = BudgetMapper.INSTANCE.toBudget(budgetDto);
            } else {
                budget = isOwner ? budgetRepo.findTopById(budgetDto.getId()) :
                        budgetRepo.findTopByIdAndUser(budgetDto.getId(), user);
            }

            // Set or update budget fields
            if (budgetDto.getId() != null && !budgetDto.getOverWrite()) {
                budget.setAmount(budget.getAmount() + budgetDto.getAmount());
                message="updated budget with category ";
            } else {
                if(budgetDto.getOverWrite()!=null && budgetDto.getOverWrite()){
                    message="replace budget with category ";
                }else{
                message="create budget with category ";
                }
                budget.setAmount(budgetDto.getAmount());
            }

            budget.setCategory(category);
            budget.setWallet(wallet);
            budget.setUser(user);

            notificationService.sendNotificationBudget(users, user.getUsername(),message,category.getName(), TypeNotification.budgetCreate,wallet.getId());
            // Save and return response
            budgetRepo.save(budget);
            return _res.createSuccessResponse(200, budget);
        } catch (Exception e) {
            return _res.createErrorResponse(e.getMessage(), 500);
        }
    }

    public ApiResponse<?> deleteBudget(String id,User user ,String walletId) {
        try {
            Wallet wallet = walletRepo.findWalletById(walletId);
            boolean isPermission = isPermission(wallet, user, Permission.Write);
            boolean exist = budgetRepo.existsById(id);
            if(isPermission){
                return _res.createErrorResponse("Can't delete budget, you don't have permission!!!", 400);
            }

            if(!exist){
                return _res.createSuccessResponse("Budget not found!!", 400);
            }

            budgetRepo.deleteById(id);
            return _res.createSuccessResponse("Delete budget successfully", 200);
        } catch (Exception e) {
            return _res.createErrorResponse(e.getMessage(), 500);
        }
    }

}
