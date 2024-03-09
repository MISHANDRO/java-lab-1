package ru.mishandro.services;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.entities.CentralBank;
import ru.mishandro.entities.Client;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.models.Money;
import ru.mishandro.models.observers.Observer;
import ru.mishandro.models.visitors.BankAccountOperationVisitor;
import ru.mishandro.services.factories.visitor.BankAccountOperationVisitorFactory;

import java.util.*;

public class BankService {
    private final BankAccountOperationVisitorFactory bankAccountOperationVisitorFactory;
    private final ClientService clientService;
    private final AccountService accountService;
    /**
     * bankId - list of client ids
     */
    private final HashMap<Integer, Set<Integer>> subscribesClients = new HashMap<>();
    private Observer observer;

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

    public void subscribe(@NotNull Client client) {
        if (!subscribesClients.containsKey(client.getBankId())) {
            subscribesClients.put(client.getBankId(), new HashSet<>());
        }

        subscribesClients.get(client.getBankId()).add(client.getId());
    }

    public void modified(@NotNull Bank bank) {
        if (observer == null) {
            return;
        }

        for (Integer clientId : subscribesClients.get(bank.getId())) {
            observer.onNext(clientService.getClientById(clientId), bank);
        }
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }
}
