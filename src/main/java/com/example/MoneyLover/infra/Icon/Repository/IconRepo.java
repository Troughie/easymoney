package com.example.MoneyLover.infra.Icon.Repository;

import com.example.MoneyLover.infra.Icon.Entity.Icon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IconRepo extends JpaRepository<Icon,String> {
}
