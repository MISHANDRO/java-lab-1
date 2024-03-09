package ru.mishandro.services.factories.bankaccounts;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Client;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.entities.accounts.DepositAccount;
import ru.mishandro.models.Time;

public class DepositAccountFactory implements BankAccountFactory {
    private final Time time;

    public DepositAccountFactory(Time time) {
        this.time = time;
    }

    @Override
    public @NotNull BankAccount create(@NotNull Client clientOwner) {
        return new DepositAccount(clientOwner.getId(), clientOwner.getBankId(), time.getCurrentDate());
    }
}
