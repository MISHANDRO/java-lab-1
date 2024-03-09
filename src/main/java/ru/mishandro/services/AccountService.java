package ru.mishandro.services;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.entities.Client;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.models.BankAccountType;
import ru.mishandro.models.Transaction;
import ru.mishandro.models.chanes.BankAccountFactoryLinkChain;
import ru.mishandro.services.repositories.AccountRepository;
import ru.mishandro.services.repositories.TransactionRepository;

import java.util.List;

public class AccountService {
    private final BankAccountFactoryLinkChain bankAccountFactorylinkChain;
    private final ClientService clientService;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(
            @NotNull BankAccountFactoryLinkChain bankAccountFactorylinkChain,
            @NotNull ClientService clientService,
            @NotNull AccountRepository accountRepository,
            @NotNull TransactionRepository transactionRepository
    ) {
        this.bankAccountFactorylinkChain = bankAccountFactorylinkChain;
        this.clientService = clientService;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public @NotNull List<BankAccount> getAccountsByBank(@NotNull Bank bank) {
        return accountRepository.getAccountsByBank(bank);
    }

    public @NotNull List<BankAccount> getAccountsByClient(@NotNull Client client) {
        return accountRepository.getAccountsByClient(client);
    }

    public BankAccount getAccountById(int id) {
        return accountRepository.getAccountById(id);
    }

    public BankAccount createBankAccountByClient(@NotNull Client client, @NotNull BankAccountType type) {
        BankAccount newBankAccount = bankAccountFactorylinkChain.create(client, type);
        if (newBankAccount == null || !accountRepository.addAccount(newBankAccount)) {
            return null;
        }

        return newBankAccount;
    }

    public boolean rollbackTransaction(@NotNull Transaction transaction) {
        if (!transaction.isCanBeRollback()) {
            return false;
        }

        BankAccount bankAccountFrom = accountRepository.getAccountById(transaction.getAccountId2());
        if (bankAccountFrom == null) {
            return false;
        }

        BankAccount bankAccountTo = accountRepository.getAccountById(transaction.getAccountId2());
        if (bankAccountTo == null) {
            return false;
        }

        bankAccountFrom.getBalance().subtract(transaction.getAmount());
        bankAccountTo.getBalance().add(transaction.getAmount());
        Transaction transactionRollback = new Transaction(
                bankAccountFrom.getId(),
                bankAccountTo.getId(),
                transaction.getAmount(),
                true,
                false
        );

        accountRepository.update(bankAccountFrom);
        accountRepository.update(bankAccountTo);

        transactionRepository.addTransaction(transactionRollback);
        return true;
    }

    public @NotNull List<Transaction> getTransactionsByAccount(@NotNull BankAccount account) {
        return transactionRepository.getTransactionsByAccount(account);
    }

    public boolean accountIsQuestionable(@NotNull BankAccount bankAccount) {
        Client client = clientService.getClientById(bankAccount.getClientOwnerId());
        if (client == null) {
            return true;
        }

        return client.getPassportNumber() == null || client.getAddress() == null;
    }
}
