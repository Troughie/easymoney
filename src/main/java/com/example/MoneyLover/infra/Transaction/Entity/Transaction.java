package com.example.MoneyLover.infra.Transaction.Entity;

import com.example.MoneyLover.infra.Budget.Entity.Budget;
import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.Recurring.Entity.Recurring;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Transations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne()
    @JsonIgnore
    User user;

    @ManyToOne()
    @JsonIgnore
    Wallet wallet;

    LocalDate date;

    long amount;

    boolean remind;

    boolean exclude;

    String notes;


    @ManyToOne
    Budget budget;

    @ManyToOne
    Category category;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Recurring recurring;

    public Transaction(Transaction trans) {
        // Copy fields, but do not copy the id
        this.user = trans.user;
        this.wallet = trans.wallet;
        this.date = trans.date;
        this.amount = trans.amount;
        this.remind = trans.remind;
        this.exclude = trans.exclude;
        this.notes = trans.notes;
        this.budget = trans.budget;
        this.category = trans.category;
        this.recurring = trans.recurring;
        // Do not copy the id field
    }
}
