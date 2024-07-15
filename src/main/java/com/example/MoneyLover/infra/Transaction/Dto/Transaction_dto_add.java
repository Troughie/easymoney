package com.example.MoneyLover.infra.Transaction.Dto;

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
public class Transaction_dto_add {

    @NotBlank
    @NotNull
    String wallet;

    @NotNull
    long amount;

    LocalDate remind;

    boolean exclude;

    String notes;

    LocalDate date;

    @NotBlank
    @NotNull
    String category;

    @NotBlank
    @NotNull
    String type;

}
