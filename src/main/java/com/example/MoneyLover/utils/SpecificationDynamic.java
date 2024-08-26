package com.example.MoneyLover.utils;


import com.example.MoneyLover.infra.Transaction.Dto.Filter_transaction;
import com.example.MoneyLover.infra.Transaction.Entity.Transaction;
import com.example.MoneyLover.infra.Wallet.Entity.Manager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SpecificationDynamic {

    public static Specification<Transaction> byFilter(Filter_transaction filter, String userID, boolean isRemind, List<Manager> managers) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getWallet() != null) {
                predicates.add(criteriaBuilder.equal(root.get("wallet").get("id"), filter.getWallet()));
            }

            if (filter.getCategory() != null) {
                // Check if getCategory returns a list or a single value

                CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("category").get("id"));

                for (String categoryId : filter.getCategory().split(",")) {
                    inClause.value(categoryId);
                }

                predicates.add(inClause);
            }

            if (managers.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userID));
            }

            if (filter.getStart() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), filter.getStart()));
            }

            if (filter.getEnd() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), filter.getEnd()));
            }

            predicates.add(criteriaBuilder.equal(root.get("remind"), isRemind));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

