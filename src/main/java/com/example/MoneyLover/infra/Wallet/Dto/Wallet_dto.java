package com.example.MoneyLover.infra.Wallet.Dto;

import com.example.MoneyLover.infra.Wallet.Entity.Manager;
import com.example.MoneyLover.infra.Wallet.Entity.WalletType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonToken;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Wallet_dto {
    String id;

    String name;

    long balance;

    long target;

    long start;

    String currency;

    String type;

    boolean main;

    LocalDate end_date;

    List<ManagerResponse> managers;

}
