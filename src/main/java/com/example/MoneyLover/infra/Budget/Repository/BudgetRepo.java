package com.example.MoneyLover.infra.Budget.Repository;

import com.example.MoneyLover.infra.Budget.Dto.Budget_response;
import com.example.MoneyLover.infra.Budget.Entity.Budget;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BudgetRepo extends JpaRepository<Budget,String> {

    @Query("select b from Budget b where b.period_end >?2 and b.wallet.id = ?1")
    List<Budget> findAllByUser( String wallet, LocalDate today);

    @Query("select b from Budget b where b.period_end <=?2 AND b.wallet.id = ?1")
    List<Budget> findAllByUserExpired( String wallet, LocalDate today);


    @Query("select b from Budget b where b.user.id=?1 and b.wallet.id=?2 and b.category.id=?3 and b.period_start<=?4 and b.period_end >=?4")
    Budget findBudgetAmount(String user, String wallet,String category,LocalDate now);

//    @Query("select new com.example.MoneyLover.infra.Budget.Dto.Budget_response(b.id ,b.category,b.amount,b.period_start,b.period_end,b.repeat_bud,b.name,b.wallet.id) from Budget b where b.user=?1")
//    List<Budget_response> findAllByUserWithoutWallet(User user);

    Budget findTopById(String id);

    Budget findTopByIdAndUser(String id,User user);
}
