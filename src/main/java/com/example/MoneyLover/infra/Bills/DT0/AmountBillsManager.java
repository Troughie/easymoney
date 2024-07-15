package com.example.MoneyLover.infra.Bills.DT0;

import java.util.List;

public record AmountBillsManager(long due_amount, long today_amount, long period_amount, List<Bill_response> bills) {
}
