package com.example.MoneyLover.infra.Bills.Service;


import com.example.MoneyLover.infra.Bills.DT0.BillRecurring;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.Entity.ApiResponse;

public interface BillService {

    ApiResponse<?> addBillRecurring(User user, BillRecurring billRecurring);

    ApiResponse<?> calculatorBillRecurring(User user);

}
