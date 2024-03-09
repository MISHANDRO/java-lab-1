package ru.mishandro.services.repositories;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Client;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTransactionRepository implements TransactionRepository {
    private List<Transaction> transactions = new ArrayList<>();

    @Override
    public boolean addTransaction(@NotNull Transaction transaction) {
        transaction.setId(transactions.size() + 1);
        transactions.add(transaction);

        return true;
    }

    @Override
    public Transaction getTransactionById(int id) {
        return transactions.stream()
                .filter(transaction -> transaction.getId() == id)
                .findFirst()
                .get();
    }

    @Override
    public @NotNull List<Transaction> getTransactionsByAccount(@NotNull BankAccount account) {
        return transactions.stream()
                .filter(transaction ->
                        transaction.getAccountId1() == account.getId() ||
                        transaction.getAccountId2() == account.getId())
                .toList();
    }
}
