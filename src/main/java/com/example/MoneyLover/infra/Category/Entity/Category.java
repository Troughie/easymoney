package com.example.MoneyLover.infra.Category.Entity;

import com.example.MoneyLover.infra.Budget.Entity.Budget;
import com.example.MoneyLover.infra.Transaction.Entity.Transaction;
import com.example.MoneyLover.infra.User.Entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Category")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;

    @ManyToOne
    User user;

    @Enumerated(EnumType.STRING)
    CategoryType categoryType;

    String categoryIcon;

    boolean defaultIncome;

    int debt_loan_type;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    List<Budget> budgets =new ArrayList<>();

    public Category(String name, CategoryType categoryType, String categoryIcon, boolean defaultIncome, int debt_loan_type) {
        this.name = name;
        this.categoryType = categoryType;
        this.categoryIcon = categoryIcon;
        this.defaultIncome = defaultIncome;
        this.debt_loan_type = debt_loan_type;
    }
}
