package com.example.MoneyLover.infra.Wallet.Entity;

import com.example.MoneyLover.infra.Budget.Entity.Budget;
import com.example.MoneyLover.infra.Transaction.Entity.Transaction;
import com.example.MoneyLover.infra.User.Entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Wallets")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne()
    User user;

    String name;

    long balance;

    long target;

    long start;

    String currency;

    String type;

    boolean main;

    LocalDate end_date;

    @OneToMany(mappedBy = "wallet",cascade = CascadeType.ALL)
    List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "wallet",cascade = CascadeType.ALL)
    @JsonIgnore
    List<Manager> managers = new ArrayList<>();

    @OneToMany(mappedBy = "wallet",cascade = CascadeType.ALL)
    @JsonIgnore
    List<Budget> budgets = new ArrayList<>();
}
