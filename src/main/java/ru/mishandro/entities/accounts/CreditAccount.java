package ru.mishandro.entities.accounts;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.models.Money;
import ru.mishandro.models.visitors.BankAccountOperationVisitor;

/**
 * An object of {@link BankAccount} that implements the functionality of a credit account
 */
public class CreditAccount extends BankAccount {

    public CreditAccount(int clientOwnerId, int bankId) {
        super(clientOwnerId, bankId);
    }

    @Override
    public @NotNull BankAccount clone() {
        BankAccount bankAccount = new CreditAccount(getClientOwnerId(), getBankId());
        bankAccount.setId(getId());
        bankAccount.balance.setValue(balance);

        return bankAccount;
    }

    @Override
    public boolean makeTransfer(@NotNull BankAccountOperationVisitor visitor, @NotNull BankAccount bankAccountTo, @NotNull Money amount) {
        if (amount.lessOrEquipThan(0) || getId() == bankAccountTo.getId()) {
            return false;
        }

        return visitor.creditMakeTransfer(this, bankAccountTo, amount);
    }

    @Override
    public boolean performOperation(@NotNull BankAccountOperationVisitor visitor, @NotNull Money amount) {
        if (amount.isNull()) {
            return false;
        }

        return visitor.creditPerformOperation(this, amount);
    }

    @Override
    public void accrualInterestOnBalanceAndDeductionOfCommission(@NotNull BankAccountOperationVisitor visitor) {
        visitor.accrualInterestOnBalanceAndDeductionOfCommission(this);
    }
}
