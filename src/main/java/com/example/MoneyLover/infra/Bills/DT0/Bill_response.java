package com.example.MoneyLover.infra.Bills.DT0;

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
public class Bill_response {
    String id;

    long amount;

    String notes;

    Category category;

    LocalDate date;

    LocalDate from_date;

    String frequency;

    int every;

    LocalDate due_date;

    boolean paid;
}
