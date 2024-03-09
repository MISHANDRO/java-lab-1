package ru.mishandro.models;

import org.jetbrains.annotations.NotNull;

public class Transaction {
    private int id = -1;
    private final boolean isSuccess_;
    private final boolean isCanBeRollback_;
    private final Money amount;
    private final int accountId1;
    private final int accountId2;

    public Transaction(
            int accountId,
            @NotNull Money amount,
            boolean isSuccess,
            boolean isCanBeRollback
    ) {
        this.accountId1 = accountId;
        this.accountId2 = accountId;
        this.amount = new Money(amount);
        this.isSuccess_ = isSuccess;
        this.isCanBeRollback_ = isCanBeRollback;
    }

    public Transaction(
            int accountId1,
            int accountId2,
            @NotNull Money amount,
            boolean isSuccess,
            boolean isCanBeRollback
    ) {
        this.accountId1 = accountId1;
        this.accountId2 = accountId2;
        this.amount = new Money(amount);
        this.isSuccess_ = isSuccess;
        this.isCanBeRollback_ = isCanBeRollback;
    }

    public static Transaction createOperation(
            int accountId,
            @NotNull Money amount,
            boolean isSuccess
    ) {
        return new Transaction(accountId, amount, isSuccess, false);
    }

    public static Transaction createTransaction(
            int accountId1,
            int accountId2,
            @NotNull Money amount,
            boolean isSuccess
    ) {
        return new Transaction(accountId1, accountId2, amount, isSuccess, true);
    }

    public int getId() {
        return id;
    }

    public boolean isSuccess() {
        return isSuccess_;
    }

    public boolean isCanBeRollback() {
        return isSuccess_ && isCanBeRollback_;
    }

    public Money getAmount() {
        return new Money(amount);
    }

    public int getAccountId1() {
        return accountId1;
    }

    public int getAccountId2() {
        return accountId2;
    }

    public void setId(int id) {
        this.id = id;
    }
}
