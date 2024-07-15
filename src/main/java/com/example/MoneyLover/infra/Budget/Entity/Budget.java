package com.example.MoneyLover.infra.Budget.Entity;

import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.Transaction.Entity.Transaction;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Budgets")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne()
    @JsonIgnore
    User user;

    @ManyToOne
    @JsonIgnore
    Category category;

    boolean repeat_bud;

    String name;

    @ManyToOne
            @JsonIgnore
    Wallet wallet;

    @OneToMany(mappedBy = "budget")
    @JsonIgnore
    List<Transaction> transactions= new ArrayList<>();

    long amount;

    LocalDate period_start;

    LocalDate period_end;
}
