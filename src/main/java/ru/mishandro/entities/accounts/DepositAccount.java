package ru.mishandro.entities.accounts;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.models.Money;
import ru.mishandro.models.visitors.BankAccountOperationVisitor;

import java.util.Date;

/**
 * An object of {@link BankAccount} that implements the functionality of a deposit account
 */
public class DepositAccount extends BankAccount {
    private final Date openDate;
    private final Money interestOnBalance = new Money(0);

    public DepositAccount(int clientOwnerId, int bankId, @NotNull Date openDate) {
        super(clientOwnerId, bankId);
        this.openDate = openDate;
    }

    @Override
    public @NotNull BankAccount clone() {
        DepositAccount bankAccount = new DepositAccount(getClientOwnerId(), getBankId(), (Date) openDate.clone());
        bankAccount.setId(getId());
        bankAccount.balance.setValue(balance);
        bankAccount.interestOnBalance.setValue(this.interestOnBalance);

        return bankAccount;
    }

    @Override
    public boolean makeTransfer(@NotNull BankAccountOperationVisitor visitor, @NotNull BankAccount bankAccountTo, @NotNull Money amount) {
        if (amount.lessOrEquipThan(0) || getId() == bankAccountTo.getId()) {
            return false;
        }

        return visitor.depositMakeTransfer(this, bankAccountTo, amount);
    }

    /**
     *  The amount ({@link Money} of interest on the balance at the moment,
     * which has not yet been credited to the account
     */
    public Money getInterestOnBalance() {
        return interestOnBalance;
    }

    /**
     *  The {@link Date} of opening of the current account
     */
    public Date getOpenDate() {
        return (Date) openDate.clone();
    }

    @Override
    public boolean performOperation(@NotNull BankAccountOperationVisitor visitor, @NotNull Money amount) {
        if (amount.isNull()) {
            return false;
        }

        return visitor.depositPerformOperation(this, amount);
    }

    @Override
    public void accrualInterestOnBalanceAndDeductionOfCommission(@NotNull BankAccountOperationVisitor visitor) {
        visitor.accrualInterestOnBalanceAndDeductionOfCommission(this);
    }
}
