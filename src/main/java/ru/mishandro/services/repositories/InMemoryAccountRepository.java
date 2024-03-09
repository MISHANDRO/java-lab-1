package ru.mishandro.services.repositories;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.entities.Client;
import ru.mishandro.entities.accounts.BankAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryAccountRepository implements AccountRepository {
    private final List<BankAccount> accounts = new ArrayList<>();

    @Override
    public boolean addAccount(@NotNull BankAccount bankAccount) {
        bankAccount.setId(accounts.size() + 1);
        accounts.add(bankAccount);

        return true;
    }

    @Override
    public @NotNull List<BankAccount> getAccountsByBank(@NotNull Bank bank) {
        return accounts.stream()
                .filter(bankAccount -> bankAccount.getBankId() == bank.getId())
                .map(BankAccount::clone)
                .toList();
    }

    @Override
    public @NotNull List<BankAccount> getAccountsByClient(@NotNull Client client) {
        return accounts.stream()
                .filter(bankAccount -> bankAccount.getClientOwnerId() == client.getId())
                .map(BankAccount::clone)
                .toList();
    }

    @Override
    public BankAccount getAccountById(int id) {
        return getBankAccountObjectById(id)
                .map(BankAccount::clone)
                .orElse(null);
    }

    @Override
    public void update(@NotNull BankAccount bankAccount) {
        Optional<BankAccount> curBankAccount = getBankAccountObjectById(bankAccount.getId());
        if (curBankAccount.isEmpty()) {
            return;
        }

        accounts.set(accounts.indexOf(curBankAccount.get()), bankAccount.clone());
    }

    private Optional<BankAccount> getBankAccountObjectById(int id) {
        return accounts.stream()
                .filter(bankAccount -> bankAccount.getId() == id)
                .findFirst();
    }
}
