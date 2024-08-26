package com.example.MoneyLover.infra.Transaction.Dto;

import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.User.Dto.UserResponse;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.Wallet.Entity.Wallet;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction_Response {

    String id;

    long amount;

    boolean remind;

    boolean exclude;

    String notes;

    Category category;

    LocalDate date;

    UserResponse user;
}
