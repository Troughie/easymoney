package com.example.MoneyLover.shares.Service;

import com.example.MoneyLover.infra.Bills.DT0.BillRecurring;
import com.example.MoneyLover.infra.Category.Entity.CategoryType;
import com.example.MoneyLover.infra.Transaction.Dto.Transaction_Recurring;
import com.example.MoneyLover.infra.Transaction.Entity.Transaction;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.Wallet.Entity.Manager;
import com.example.MoneyLover.infra.Wallet.Entity.Permission;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceExtended {
    public List<Transaction> getTransactionsByTypeAndDate(Wallet wallet, CategoryType type, LocalDate date) {

        return wallet.getTransactions().stream()
                .filter(tran -> tran.getCategory().getCategoryType().equals(type) && tran.getRecurring() ==null)
                .filter(tran -> {
                            if(date!=null){
                                    return tran.getDate().isBefore(date)||tran.getDate().isEqual(date);
                            }else{
                                return tran.getDate().isBefore(LocalDate.now())||tran.getDate().isEqual(LocalDate.now());
                            }
                        }
                )
                .filter(tran -> {
                    // Get the current user's ID
                    String userId = tran.getUser().getId();

                    // Collect manager userIds from wallet
                    List<String> managerUserIds = wallet.getManagers().stream()
                            .map(manager -> manager.getUser().getId())
                            .toList();

                    // Include transactions matching the current user's ID or any of the manager userIds
                    return userId.equals(wallet.getUser().getId()) || managerUserIds.contains(userId);
                })
                .collect(Collectors.toList());
    }

    public long calculateTotalAmount(List<Transaction> transactions) {
        return transactions.stream()
                .mapToLong(Transaction::getAmount)
                .sum();
    }

    public boolean isPermission(Wallet wallet,User user, Permission permission) {
        Manager manager = wallet.getManagers().stream().filter(m->m.getUser().getId().equals(user.getId())).findFirst().orElse(null);
        boolean Owner =wallet.getUser().getId().equals(user.getId());
        if(Owner) return false;
        return (manager!=null && !(manager.getPermission().equals(permission) || manager.getPermission().equals(Permission.All)));
    }

    public LocalDate firstDayOfMonth(){
        LocalDate today = LocalDate.now();

        // Lấy tháng và năm hiện tại
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);

        return  yearMonth.atDay(1);
    }

    public LocalDate lastDayOfMonth() {
        LocalDate today = LocalDate.now();

        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);
         return  yearMonth.atEndOfMonth();
    }

    public LocalDate calculateFromDateScheduled(Transaction_Recurring transactionRecurring, BillRecurring billRecurring){
        LocalDate date;
        String frequency;
        int every;
        int dateInWeek;
        if(transactionRecurring!=null){
             date = transactionRecurring.getFrom_date();
            frequency=transactionRecurring.getFrequency();
            every=transactionRecurring.getEvery();
            dateInWeek=transactionRecurring.getDate_of_week();
        } else{
             date = billRecurring.getFrom_date();
            frequency=billRecurring.getFrequency();
            every=billRecurring.getEvery();
            dateInWeek=billRecurring.getDate_of_week();
        }

        return switch (frequency) {
            case "days" -> date.plusDays(every);
            case "weeks" -> {
                int day = date.plusWeeks(every).getDayOfWeek().getValue();
                if(dateInWeek-day>0){
                    yield date.plusWeeks(every).plusDays(dateInWeek-day);
                }else{
                    yield date.plusWeeks(every).minusDays(day-dateInWeek);
                }
            }
            case "months" -> date.plusMonths(every);
            case "years" -> date.plusYears(every);
            default -> date;
        };
    }
}
