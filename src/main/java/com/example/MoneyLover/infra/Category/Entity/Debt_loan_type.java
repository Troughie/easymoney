package com.example.MoneyLover.infra.Category.Entity;

public enum Debt_loan_type {
    Debt(1),
    Loan(2);
    private final int value;

    Debt_loan_type(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Debt_loan_type fromValue(int value) {
        for (Debt_loan_type type : Debt_loan_type.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
