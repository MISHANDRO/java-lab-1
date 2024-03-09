package ru.mishandro.services.factories.bankaccounts;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Client;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.entities.accounts.DebitAccount;

public class DebitAccountFactory implements BankAccountFactory {
    @Override
    public @NotNull BankAccount create(@NotNull Client clientOwner) {
        return new DebitAccount(clientOwner.getId(), clientOwner.getBankId());
    }
}
