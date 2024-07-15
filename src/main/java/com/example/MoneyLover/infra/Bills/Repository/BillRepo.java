package com.example.MoneyLover.infra.Bills.Repository;

import com.example.MoneyLover.infra.Bills.DT0.Bill_response;
import com.example.MoneyLover.infra.Bills.Entity.Bill;
import com.example.MoneyLover.infra.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BillRepo extends JpaRepository<Bill,String> {
    @Query("select new com.example.MoneyLover.infra.Bills.DT0.Bill_response(b.id ,b.amount,b.notes,b.category,b.date,b.recurring.from_date,b.recurring.frequency,b.recurring.every,b.recurring.due_date,b.paid) from Bill b where b.user = ?1")
    List<Bill_response> findAllByUser(User user);

    @Query("select b from Bill b where b.recurring=null")
    List<Bill> findBills();

    @Query("select new com.example.MoneyLover.infra.Bills.DT0.Bill_response(b.id ,b.amount,b.notes,b.category,b.date,b.recurring.from_date,b.recurring.frequency,b.recurring.every,b.recurring.due_date,b.paid) from Bill b where b.user = ?1 and b.date=?2")
    List<Bill_response> listBillToDay(User user, LocalDate today);

    @Query("select new com.example.MoneyLover.infra.Bills.DT0.Bill_response(b.id ,b.amount,b.notes,b.category,b.date,b.recurring.from_date,b.recurring.frequency,b.recurring.every,b.recurring.due_date,b.paid) from Bill b where b.user = ?1 and b.recurring.due_date!=null")
    List<Bill_response> listBillExpired(User user);

}

