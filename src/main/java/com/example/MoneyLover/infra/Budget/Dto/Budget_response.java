package com.example.MoneyLover.infra.Budget.Dto;

import com.example.MoneyLover.infra.Category.Entity.Category;
import com.example.MoneyLover.infra.User.Dto.UserResponse;
import com.example.MoneyLover.infra.User.Entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Budget_response {
    String id;

    Category category;

    long amount;

    LocalDate period_start;

    LocalDate period_end;

    boolean repeat_bud;

    String name;

    String wallet;

    UserResponse user;
}
