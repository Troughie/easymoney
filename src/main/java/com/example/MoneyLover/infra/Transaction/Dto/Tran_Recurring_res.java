package com.example.MoneyLover.infra.Transaction.Dto;

import com.example.MoneyLover.infra.Category.Entity.Category;
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
public class Tran_Recurring_res {
    String id;

    long amount;

    boolean remind;

    boolean exclude;

    String notes;

    Category category;

    LocalDate date;

    boolean done;
}
