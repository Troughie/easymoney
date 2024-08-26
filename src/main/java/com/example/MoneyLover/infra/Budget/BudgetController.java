package com.example.MoneyLover.infra.Budget;

import com.example.MoneyLover.infra.Budget.Dto.Budget_Dto;
import com.example.MoneyLover.infra.Budget.Repository.BudgetRepo;
import com.example.MoneyLover.infra.Budget.Service.BudgetService;
import com.example.MoneyLover.infra.Category.Dto.CateConstant;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BudgetController {

    private final ResponseException _res;

    private final BudgetService budgetService;

    @GetMapping("budgets")
    public ResponseEntity<?> index(@AuthenticationPrincipal User user,
                                   @RequestParam(required = false) String wallet,
                                   @RequestParam(required = false) String type
    )
    {
        var result =budgetService.getBudget(user,wallet,type);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("budget/add")
    public ResponseEntity<?> add(@AuthenticationPrincipal User user,
                                 @Valid @RequestBody Budget_Dto budgetDto
                                 )
    {
        var result =budgetService.saveBudget(user,budgetDto);
        return _res.responseEntity(result,result.getCode());
    }

    @DeleteMapping("budget/delete/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user,@PathVariable String id,@RequestBody String walletId)
    {
        walletId = walletId.replace("\"", "");
        var result =budgetService.deleteBudget(id,user,walletId);
        return _res.responseEntity(result,result.getCode());
    }
}
