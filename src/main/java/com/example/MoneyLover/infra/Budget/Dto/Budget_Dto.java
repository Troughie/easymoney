package com.example.MoneyLover.infra.Budget.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Null;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Budget_Dto {
    String id;
    String category;

    long amount;

    LocalDate period_start;

    LocalDate period_end;

    boolean repeat_bud;
    
    String wallet;

    String name;

    Boolean overWrite;
}
