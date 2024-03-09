package ru.mishandro.models.visitors;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.entities.accounts.CreditAccount;
import ru.mishandro.entities.accounts.DebitAccount;
import ru.mishandro.entities.accounts.DepositAccount;
import ru.mishandro.models.Money;
import ru.mishandro.models.Transaction;
import ru.mishandro.services.repositories.AccountRepository;
import ru.mishandro.services.repositories.TransactionRepository;

public class RepositoryUpdateAndTransactionVisitor implements BankAccountOperationVisitor {
    private BankAccountOperationVisitor visitor;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public RepositoryUpdateAndTransactionVisitor(
            @NotNull AccountRepository accountRepository,
            @NotNull TransactionRepository transactionRepository
    ) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public void setVisitor(BankAccountOperationVisitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public boolean debitMakeTransfer(@NotNull DebitAccount accountFrom, @NotNull BankAccount accountTo, @NotNull Money amount) {
        boolean result = visitor.debitMakeTransfer(accountFrom, accountTo, amount);
        createOperationTransaction(accountFrom, accountTo, amount, result);

        return result;
    }

    @Override
    public boolean depositMakeTransfer(@NotNull DepositAccount accountFrom, @NotNull BankAccount accountTo, @NotNull Money amount) {
        boolean result = visitor.depositMakeTransfer(accountFrom, accountTo, amount);
        createOperationTransaction(accountFrom, accountTo, amount, result);

        return result;
    }

    @Override
    public boolean creditMakeTransfer(@NotNull CreditAccount accountFrom, @NotNull BankAccount accountTo, @NotNull Money amount) {
        boolean result = visitor.creditMakeTransfer(accountFrom, accountTo, amount);
        createOperationTransaction(accountFrom, accountTo, amount, result);

        return result;
    }

    @Override
    public boolean debitPerformOperation(@NotNull DebitAccount account, @NotNull Money amount) {
        boolean result = visitor.debitPerformOperation(account, amount);
        createOperation(account, amount, result);

        return result;
    }

    @Override
    public boolean depositPerformOperation(@NotNull DepositAccount account, @NotNull Money amount) {
        boolean result = visitor.depositPerformOperation(account, amount);
        createOperation(account, amount, result);

        return result;
    }

    @Override
    public boolean creditPerformOperation(@NotNull CreditAccount account, @NotNull Money amount) {

        boolean result = visitor.creditPerformOperation(account, amount);
        createOperation(account, amount, result);

        return result;
    }

    @Override
    public Money accrualInterestOnBalanceAndDeductionOfCommission(@NotNull DebitAccount account) {
        Money result = visitor.accrualInterestOnBalanceAndDeductionOfCommission(account);
        createAboutAccrualAndDeduction(account, result);

        return result;
    }

    @Override
    public Money accrualInterestOnBalanceAndDeductionOfCommission(@NotNull DepositAccount account) {
        Money result = visitor.accrualInterestOnBalanceAndDeductionOfCommission(account);
        createAboutAccrualAndDeduction(account, result);

        return result;
    }

    @Override
    public Money accrualInterestOnBalanceAndDeductionOfCommission(@NotNull CreditAccount account) {
        Money result = visitor.accrualInterestOnBalanceAndDeductionOfCommission(account);
        createAboutAccrualAndDeduction(account, result);

        return result;
    }


    private void createOperation(@NotNull BankAccount account, @NotNull Money amount, boolean result) {
        if (result) {
            accountRepository.update(account);
        }

        Transaction transaction = Transaction.createOperation(account.getId(), amount, result);
        transactionRepository.addTransaction(transaction);
    }

    private void createOperationTransaction(
            @NotNull BankAccount accountFrom,
            @NotNull BankAccount accountTo,
            @NotNull Money amount,
            boolean result
    ) {
        if (result) {
            accountRepository.update(accountFrom);
            accountRepository.update(accountTo);
        }

        Transaction transaction = Transaction.createTransaction(
                accountFrom.getId(), accountTo.getId(), amount, result);
        transactionRepository.addTransaction(transaction);
    }

    private void createAboutAccrualAndDeduction(@NotNull BankAccount account, Money money) {
        if (money == null) {
            accountRepository.update(account);
            return;
        }

        createOperation(account, money, true);
    }
}
