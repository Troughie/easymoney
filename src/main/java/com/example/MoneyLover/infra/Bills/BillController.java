package com.example.MoneyLover.infra.Bills;

import com.example.MoneyLover.infra.Bills.DT0.BillRecurring;
import com.example.MoneyLover.infra.Bills.Service.BillService;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BillController {
    private final ResponseException _res;

    private final BillService billService;

    @PostMapping("bill/add")
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @RequestBody BillRecurring billRecurring)
    {
        var result =billService.addBillRecurring(user,billRecurring);
        return _res.responseEntity(result,result.getCode());
    }

    @GetMapping("bills")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user)
    {
        var result =billService.calculatorBillRecurring(user);
        return _res.responseEntity(result,result.getCode());
    }
}
