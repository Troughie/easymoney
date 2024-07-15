package com.example.MoneyLover.infra.Wallet.Repository;

import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.List;

public interface WalletRepo extends JpaRepository<Wallet,String>,ListPagingAndSortingRepository<Wallet,String> {

    Page<Wallet> findAllByUser(User user, Pageable pageable);

    List<Wallet> findAllByUser(User user);

    Wallet findWalletById(String id);

    @Query("select w from Wallet w where w.user=?1 and w.main=true")
    Wallet findWalletIsMainTrue(User user);
}
