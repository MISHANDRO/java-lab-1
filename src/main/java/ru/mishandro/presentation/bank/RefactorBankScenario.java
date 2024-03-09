package ru.mishandro.presentation.bank;

import ru.mishandro.entities.Bank;
import ru.mishandro.presentation.abstractions.Scenario;

public class RefactorBankScenario implements Scenario {
    private final Bank bank;

    @Override
    public boolean handle() {
        return false; // TODO
    }

    public RefactorBankScenario(Bank bank) {
        this.bank = bank;
    }
}
