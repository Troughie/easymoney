package com.example.MoneyLover.shares.Service;

import com.example.MoneyLover.infra.Bills.DT0.BillRecurring;
import com.example.MoneyLover.infra.Category.Entity.CategoryType;
import com.example.MoneyLover.infra.Recurring.Entity.Recurring;
import com.example.MoneyLover.infra.Transaction.Dto.Transaction_Recurring;
import com.example.MoneyLover.infra.Transaction.Entity.Transaction;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceExtended {
    public List<Transaction> getTransactionsByTypeAndDate(Wallet wallet, CategoryType type, LocalDate date) {

        return wallet.getTransactions().stream()
                .filter(tran -> tran.getCategory().getCategoryType().equals(type))
                .filter(tran -> {
                            if(date!=null){
                                return tran.getDate().isBefore(date)||tran.getDate().isEqual(date);
                            }else{
                                return tran.getDate().isAfter(LocalDate.now())||tran.getDate().isEqual(LocalDate.now());
                            }
                        }
                )
                .collect(Collectors.toList());
    }

    public long calculateTotalAmount(List<Transaction> transactions) {
        return transactions.stream()
                .mapToLong(Transaction::getAmount)
                .sum();
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
