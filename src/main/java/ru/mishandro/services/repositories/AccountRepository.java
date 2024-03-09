package ru.mishandro.services.repositories;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.entities.Client;
import ru.mishandro.entities.accounts.BankAccount;

import java.util.List;

public interface AccountRepository {
    boolean addAccount(@NotNull BankAccount bankAccount);
    @NotNull List<BankAccount> getAccountsByBank(@NotNull Bank bank);
    @NotNull List<BankAccount> getAccountsByClient(@NotNull Client client);
    BankAccount getAccountById(int id);

    void update(@NotNull BankAccount bankAccount);
}
