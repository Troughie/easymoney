package com.example.MoneyLover.infra.Transaction.Repository;

import com.example.MoneyLover.infra.Transaction.Dto.Tran_Recurring_res;
import com.example.MoneyLover.infra.Transaction.Dto.Transaction_Response;
import com.example.MoneyLover.infra.Transaction.Entity.Transaction;
import com.example.MoneyLover.infra.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction,String> , JpaSpecificationExecutor<Transaction> {

    @Query("select  new com.example.MoneyLover.infra.Transaction.Dto.Transaction_Response(t.id,t.amount,t.remind,t.exclude,t.notes,t.category,t.date) from Transaction t where t.user=?1")
    List<Transaction_Response> getAll(User user);

    @Query("select  new com.example.MoneyLover.infra.Transaction.Dto.Transaction_Response(t.id,t.amount,t.remind,t.exclude,t.notes,t.category,t.date) from Transaction t where t.wallet.id=?1 and t.date between ?2 and ?3")
    List<Transaction_Response> getAllInMonth(String wallet_id, LocalDate start , LocalDate end);

    @Query("select  new com.example.MoneyLover.infra.Transaction.Dto.Transaction_Response(t.id,t.amount,t.remind,t.exclude,t.notes,t.category,t.date) from Transaction t where t.user=?1 and t.remind=true")
    List<Transaction_Response> getTranWithWallet(User user);

    @Query("select  new com.example.MoneyLover.infra.Transaction.Dto.Tran_Recurring_res(t.id,t.amount,t.remind,t.exclude,t.notes,t.category,t.recurring.from_date,t.recurring.done) from Transaction t where t.user=?1 and t.remind=true")
    List<Tran_Recurring_res> getTranRecurring(User user);


    @Query("select t from Transaction t where t.remind=true")
    List<Transaction> getTrans();
}
