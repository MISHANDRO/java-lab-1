package ru.mishandro.services.repositories;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;

import java.util.List;

public interface BankRepository {
    @NotNull List<Bank> getAllBanks();
    boolean addBank(@NotNull Bank bank);
    Bank getBankById(int bankId);
    Bank getBankByName(@NotNull String name);
    void update(@NotNull Bank bank);
}
