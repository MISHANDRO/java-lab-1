package ru.mishandro.entities.accounts;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.models.BankAccountType;
import ru.mishandro.models.Money;
import ru.mishandro.models.visitors.BankAccountOperationVisitor;

/**
 * The main abstraction of bank accounts
 */
abstract public class BankAccount {
    private int id = -1;
    private final int clientOwnerId;
    private final int bankId;

    public BankAccount(int clientOwnerId, int bankId) {
        this.clientOwnerId = clientOwnerId;
        this.bankId = bankId;
    }

    protected Money balance = new Money();

    /**
     *  Bank account's id
     */
    public int getId() {
        return id;
    }

    /**
     *  Bank account's of type {@link ru.mishandro.models.Money}
     */
    public Money getBalance() {
        return balance;
    }

    /**
     *  {@link ru.mishandro.entities.Client} owner id, who owns this account
     */
    public int getClientOwnerId() {
        return clientOwnerId;
    }

    /**
     * return id of the {@link ru.mishandro.entities.Bank} where the bank account is opened
     */
    public int getBankId() {
        return bankId;
    }

    /**
     * Set id (only for this object!)
     * @param id new id for this object
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * return new copy of bank account object
     */
    public abstract @NotNull BankAccount clone();

    /**
     * Make a transfer to another bank account (only within these objects!)
     * @param visitor Object of type {@link BankAccountOperationVisitor}, which describe the transfer rules for different account types
     * @param bankAccountTo {@link BankAccount}, which accepts a transfer
     * @param amount Amount of a transfer, type: {@link Money}
     * @return Result of a transfer: success/unsuccess
     */
    public abstract boolean makeTransfer(
            @NotNull BankAccountOperationVisitor visitor,
            @NotNull BankAccount bankAccountTo,
            @NotNull Money amount
    );

    /**
     * Make a withdrawal or deposit of money
     * @param visitor Object of type {@link BankAccountOperationVisitor}, which describe the operation rules for different account types
     * @param amount Positive for deposit, negative for withdrawal
     * @return Result of an operation: success/unsuccess
     */
    public abstract boolean performOperation(@NotNull BankAccountOperationVisitor visitor, @NotNull Money amount);

    /**
     * The method for calculating the percentage of the balance or commission at the current time
     * @param visitor Object of type {@link BankAccountOperationVisitor}, which describe the calculating rules for different account types
     */
    public abstract void accrualInterestOnBalanceAndDeductionOfCommission(@NotNull BankAccountOperationVisitor visitor);
}
