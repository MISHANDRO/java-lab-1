package ru.mishandro.services.factories.bankaccounts;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Client;
import ru.mishandro.entities.accounts.BankAccount;

public interface BankAccountFactory {
    @NotNull BankAccount create(@NotNull Client clientOwner);
}
