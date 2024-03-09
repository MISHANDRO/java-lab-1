package ru.mishandro.models.chanes;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Client;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.models.BankAccountType;
import ru.mishandro.services.factories.bankaccounts.BankAccountFactory;

public class BankAccountFactoryLinkChain {
    private BankAccountFactoryLinkChain next;
    private final BankAccountType curType;
    private final BankAccountFactory factory;

    public BankAccountFactoryLinkChain(
            @NotNull BankAccountType curType,
            @NotNull BankAccountFactory factory) {
        this.curType = curType;
        this.factory = factory;
    }

    public void setNext(@NotNull BankAccountFactoryLinkChain newNext) {
        if (next == null) {
            next = newNext;
        } else {
            next.setNext(newNext);
        }
    }

    public BankAccount create(@NotNull Client clientOwner, @NotNull BankAccountType type) {
        if (type == curType) {
            return factory.create(clientOwner);
        }

        if (next == null) {
            return null;
        }

        return next.create(clientOwner, type);
    }
}
