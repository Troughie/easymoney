package com.example.MoneyLover.infra.Category.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryAdd {
    @NotBlank
    @NotNull
    String name;

    @NotBlank
    @NotNull
    String icon;

    @NotBlank
    @NotNull
    String type;
}
