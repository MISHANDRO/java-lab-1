package ru.mishandro.services;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.entities.CentralBank;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.models.Money;
import ru.mishandro.models.visitors.BankAccountOperationVisitor;
import ru.mishandro.services.factories.visitor.BankAccountOperationVisitorFactory;

public class BankService {
    private final BankAccountOperationVisitorFactory bankAccountOperationVisitorFactory;
    private final ClientService clientService;
    private final AccountService accountService;

    public BankService(
            @NotNull ClientService clientService,
            @NotNull AccountService accountService,
            @NotNull BankAccountOperationVisitorFactory bankAccountOperationVisitorFactory) {
        this.clientService = clientService;
        this.accountService = accountService;
        this.bankAccountOperationVisitorFactory = bankAccountOperationVisitorFactory;
    }

    public void accrualOfInterestAndCommission(@NotNull Bank bank) {
        BankAccountOperationVisitor visitor = bankAccountOperationVisitorFactory.create(bank);
        for (BankAccount bankAccount : accountService.getAccountsByBank(bank)) {
            bankAccount.accrualInterestOnBalanceAndDeductionOfCommission(visitor);
        }
    }

    public boolean performOperationWithAccount(@NotNull BankAccount bankAccount, @NotNull Money amount) {
        BankAccount curAccount = accountService.getAccountById(bankAccount.getId());
        if (curAccount == null) {
            return false;
        }

        Bank curBank = CentralBank.instance().getBankById(curAccount.getBankId());
        if (curBank == null) {
            return false;
        }

        return curAccount.performOperation(bankAccountOperationVisitorFactory.create(curBank), amount);
    }

    public boolean makeTransfer(
            @NotNull BankAccount bankAccountFrom,
            @NotNull BankAccount bankAccountTo,
            @NotNull Money amount
    ) {
        BankAccount curAccountFrom = accountService.getAccountById(bankAccountFrom.getId());
        if (curAccountFrom == null) {
            return false;
        }

        Bank curBank = CentralBank.instance().getBankById(curAccountFrom.getBankId());
        if (curBank == null) {
            return false;
        }

        BankAccount curAccountTo = accountService.getAccountById(bankAccountTo.getId());
        if (curAccountTo == null) {
            return false;
        }

        return curAccountFrom.makeTransfer(
                bankAccountOperationVisitorFactory.create(curBank), curAccountTo, amount);
    }
}
