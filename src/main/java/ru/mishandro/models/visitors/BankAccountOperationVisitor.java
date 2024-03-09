package ru.mishandro.models.visitors;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.entities.accounts.CreditAccount;
import ru.mishandro.entities.accounts.DebitAccount;
import ru.mishandro.entities.accounts.DepositAccount;
import ru.mishandro.models.Money;

public interface BankAccountOperationVisitor {
    boolean debitMakeTransfer(@NotNull DebitAccount accountFrom, @NotNull BankAccount accountTo, @NotNull Money amount);
    boolean depositMakeTransfer(@NotNull DepositAccount accountFrom, @NotNull BankAccount accountTo,  @NotNull Money amount);
    boolean creditMakeTransfer(@NotNull CreditAccount accountFrom, @NotNull BankAccount accountTo,  @NotNull Money amount);


    boolean debitPerformOperation(@NotNull DebitAccount account, @NotNull Money amount);
    boolean depositPerformOperation(@NotNull DepositAccount account, @NotNull Money amount);
    boolean creditPerformOperation(@NotNull CreditAccount account, @NotNull Money amount);

    Money accrualInterestOnBalanceAndDeductionOfCommission(@NotNull DebitAccount account);
    Money accrualInterestOnBalanceAndDeductionOfCommission(@NotNull DepositAccount account);
    Money accrualInterestOnBalanceAndDeductionOfCommission(@NotNull CreditAccount account);
}
