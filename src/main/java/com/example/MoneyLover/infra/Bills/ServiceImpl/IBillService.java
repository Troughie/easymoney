package com.example.MoneyLover.infra.Bills.ServiceImpl;

import com.example.MoneyLover.infra.Bills.DT0.AmountBillsManager;
import com.example.MoneyLover.infra.Bills.DT0.BillRecurring;
import com.example.MoneyLover.infra.Bills.DT0.Bill_response;
import com.example.MoneyLover.infra.Bills.Entity.Bill;
import com.example.MoneyLover.infra.Bills.Mapper.BillMapper;
import com.example.MoneyLover.infra.Bills.Repository.BillRepo;
import com.example.MoneyLover.infra.Bills.Service.BillService;
import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.Category.Repository.CategoryRepo;
import com.example.MoneyLover.infra.Recurring.Entity.Recurring;
import com.example.MoneyLover.infra.Recurring.Repository.RecurringRepo;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
import com.example.MoneyLover.infra.Wallet.Repository.WalletRepo;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import com.example.MoneyLover.shares.Service.ServiceExtended;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IBillService extends ServiceExtended implements BillService {
    private final ResponseException _res;

    private final RecurringRepo recurringRepo;

    private final CategoryRepo categoryRepo;

    private final WalletRepo walletRepo;

    private final BillRepo billRepo;

    public ApiResponse<?> addBillRecurring(User user, BillRecurring billRecurring) {
        try {
            LocalDate create_date = billRecurring.getFrom_date();
            LocalDate date =calculateFromDateScheduled(null,billRecurring);
            Recurring recurring = BillMapper.INSTANCE.toBillRecurring(billRecurring);
            recurring.setFrom_date(date);
            Recurring rec = recurringRepo.save(recurring);
            Bill bill = BillMapper.INSTANCE.toBill(billRecurring);
            Category category = categoryRepo.findCategoriesById(billRecurring.getCategory());
            Wallet wallet = walletRepo.findWalletById(billRecurring.getWallet());
            bill.setWallet(wallet);
            bill.setCategory(category);
            bill.setRecurring(rec);
            bill.setUser(user);
            bill.setDate(create_date);
            billRepo.save(bill);
            return _res.createSuccessResponse("Add bill successfully",200,bill);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> calculatorBillRecurring(User user) {
        try {
            LocalDate today = LocalDate.now();
            List<Bill_response> billsToDay =billRepo.listBillToDay(user,today);
            List<Bill_response> billsPeriod =billRepo.findAllByUser(user);
            List<Bill_response> billsExpired =billRepo.listBillExpired(user);
            long amountBillToDay = CalculatorAmountBill(billsToDay);
            long amountBillPeriod = CalculatorAmountBill(billsPeriod);
            long amountBillExpired = CalculatorAmountBill(billsExpired);

            return _res.createSuccessResponse("Get bill successfully",200,
                    new AmountBillsManager(amountBillExpired,amountBillToDay,amountBillPeriod,billsPeriod));
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    private long CalculatorAmountBill(List<Bill_response> bills)
    {
        return bills.stream().mapToLong(Bill_response::getAmount).sum();
    }

}
