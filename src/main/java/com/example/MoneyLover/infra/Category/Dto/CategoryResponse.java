package com.example.MoneyLover.infra.Category.Dto;

import com.example.MoneyLover.infra.Category.Entity.CategoryType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {

    String id;

    String name;

    String categoryIcon;

    CategoryType categoryType;
}
