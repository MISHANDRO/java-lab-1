package ru.mishandro.entities.accounts;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.models.Money;
import ru.mishandro.models.visitors.BankAccountOperationVisitor;

/**
 * An object of {@link BankAccount} that implements the functionality of a debit account
 */
public class DebitAccount extends BankAccount {
    private final Money interestOnBalance = new Money(0);

    public DebitAccount(int clientOwnerId, int bankId) {
        super(clientOwnerId, bankId);
    }

    @Override
    public @NotNull BankAccount clone() {
        DebitAccount bankAccount = new DebitAccount(getClientOwnerId(), getBankId());
        bankAccount.setId(getId());
        bankAccount.balance.setValue(balance);
        bankAccount.interestOnBalance.setValue(interestOnBalance);

        return bankAccount;
    }

    @Override
    public boolean makeTransfer(@NotNull BankAccountOperationVisitor visitor, @NotNull BankAccount bankAccountTo, @NotNull Money amount) {
        if (amount.lessOrEquipThan(0) || getId() == bankAccountTo.getId()) {
            return false;
        }

        return visitor.debitMakeTransfer(this, bankAccountTo, amount);
    }

    /**
     *  The amount ({@link Money} of interest on the balance at the moment,
     * which has not yet been credited to the account
     */
    public Money getInterestOnBalance() {
        return interestOnBalance;
    }

    @Override
    public boolean performOperation(@NotNull BankAccountOperationVisitor visitor, @NotNull Money amount) {
        if (amount.isNull()) {
            return false;
        }

        return visitor.debitPerformOperation(this, amount);
    }

    @Override
    public void accrualInterestOnBalanceAndDeductionOfCommission(@NotNull BankAccountOperationVisitor visitor) {
        visitor.accrualInterestOnBalanceAndDeductionOfCommission(this);
    }
}
