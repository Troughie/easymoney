package com.example.MoneyLover.infra.Wallet.Entity;

public enum WalletType {
    BasicWallet("basic"),
    GoalWallet("goal"),
    ;

    private final String name;
    WalletType(String name) {
        this.name =name;
    }
    public String getName() {
        return name;
    }

}
