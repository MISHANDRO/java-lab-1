package ru.mishandro.services.repositories;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.entities.Client;

import java.util.ArrayList;
import java.util.List;

public class InMemoryBankRepository implements BankRepository {
    private final List<Bank> banks = new ArrayList<>();

    @Override
    public @NotNull List<Bank> getAllBanks() {
        return banks.stream().map(Bank::clone).toList();
    }

    @Override
    public boolean addBank(@NotNull Bank bank) {
        bank.setId(banks.size() + 1);
        banks.add(bank);

        return true;
    }

    @Override
    public Bank getBankById(int bankId) {
        return banks.stream()
                .filter(bank -> bank.getId() == bankId)
                .findFirst()
                .map(Bank::clone)
                .orElse(null);
    }

    @Override
    public Bank getBankByName(@NotNull String name) {
        return banks.stream()
                .filter(bank -> bank.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(Bank::clone)
                .orElse(null);
    }

    @Override
    public void update(@NotNull Bank bank) {
        Bank curBank = getBankById(bank.getId());
        if (curBank == null) {
            return;
        }

        banks.set(banks.indexOf(curBank), curBank.clone());
    }
}
