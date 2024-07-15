package com.example.MoneyLover.infra.Transaction.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction_Recurring {
    @NotBlank
    @NotNull
    String wallet;

    @NotNull
    long amount;

    String notes;

    @NotBlank
    @NotNull
    String category;

    String frequency;

    int every;

    LocalDate from_date;

    LocalDate to_date;

    boolean forever;

    int date_of_week;

    int for_time;
}
