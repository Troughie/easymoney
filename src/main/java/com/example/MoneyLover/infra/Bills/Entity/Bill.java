package com.example.MoneyLover.infra.Bills.Entity;

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
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Bills")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bill {
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

    String notes;

    boolean paid;

    @ManyToOne
    Category category;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Recurring recurring;

    public Bill(Bill bill) {
        // Copy fields, but do not copy the id
        this.user = bill.user;
        this.wallet = bill.wallet;
        this.date = bill.date;
        this.amount = bill.amount;
        this.notes = bill.notes;
        this.category = bill.category;
        this.recurring = bill.recurring;
    }
}
