package ru.mishandro.services.factories.bankaccounts;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Client;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.entities.accounts.CreditAccount;

public class CreditAccountFactory implements BankAccountFactory {
    @Override
    public @NotNull BankAccount create(@NotNull Client clientOwner) {
        return new CreditAccount(clientOwner.getId(), clientOwner.getBankId());
    }
}
