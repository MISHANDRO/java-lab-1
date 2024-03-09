package ru.mishandro.services.repositories;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.models.Transaction;

import java.util.List;

public interface TransactionRepository {
    boolean addTransaction(@NotNull Transaction transaction);

    Transaction getTransactionById(int id);
    @NotNull List<Transaction> getTransactionsByAccount(@NotNull BankAccount account);
}
