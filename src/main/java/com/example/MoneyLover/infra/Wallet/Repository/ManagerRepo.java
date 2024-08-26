package com.example.MoneyLover.infra.Wallet.Repository;

import com.example.MoneyLover.infra.Wallet.Entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepo extends JpaRepository<Manager, String> {
}
