package ru.mishandro.presentation.client;

import ru.mishandro.entities.Bank;
import ru.mishandro.presentation.abstractions.Scenario;

public class CreateClientScenario implements Scenario {
    public final Bank bank;

    public CreateClientScenario(Bank bank) {
        this.bank = bank;
    }

    @Override
    public boolean handle() {
        return false; // TODO
    }
}
