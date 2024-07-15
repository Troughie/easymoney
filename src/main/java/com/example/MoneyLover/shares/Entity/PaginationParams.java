package com.example.MoneyLover.shares.Entity;

import com.example.MoneyLover.shares.Constants.PaginationConstant;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PaginationParams {

    private Integer pageNumber = Integer.parseInt(PaginationConstant.PAGE_NUMBER);
    private Integer documentsPerPage = Integer.parseInt(PaginationConstant.DOCUMENTS_PER_PAGE);
    private String sort = PaginationConstant.SORTING;
    private String field = PaginationConstant.FIELD;
}
