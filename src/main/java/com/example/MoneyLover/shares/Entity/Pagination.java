package com.example.MoneyLover.shares.Entity;

import java.util.List;

public record Pagination<T>(List<T> content,
                            Integer totalPage,
                            Integer documentsPerPage,
                            int pageNumber,
                            long totalDocument,
                            boolean isLast) {
}
