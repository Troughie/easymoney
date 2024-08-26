package com.example.MoneyLover.infra.Wallet.Entity;

import com.example.MoneyLover.infra.User.Entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Managers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JsonIgnore
    Wallet wallet;

    @ManyToOne
    @JsonIgnore
    User user;

    @Enumerated(EnumType.STRING)
    Permission permission;

    public Manager(Wallet wallet, User users, Permission permission) {
        this.wallet = wallet;
        this.user = users;
        this.permission = permission;
    }
}
