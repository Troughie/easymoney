package com.example.MoneyLover.infra.Recurring.Repository;

import com.example.MoneyLover.infra.Recurring.Entity.Recurring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringRepo extends JpaRepository<Recurring,String> {
}
