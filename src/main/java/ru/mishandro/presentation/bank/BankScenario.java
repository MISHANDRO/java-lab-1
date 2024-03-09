package ru.mishandro.presentation.bank;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.presentation.abstractions.ChosenScenario;
import ru.mishandro.presentation.abstractions.Scenario;
import ru.mishandro.presentation.client.ChooseClientScenario;
import ru.mishandro.presentation.client.CreateClientScenario;

import java.time.Duration;

public class BankScenario extends ChosenScenario<Scenario> implements Scenario {
    private final Bank bank;

    public BankScenario(@NotNull Bank bank) {
        this.bank = bank;
        setup();
    }

    @Override
    public boolean handle() {
        Scenario scenario = read();
        if (scenario != null) {
            scenario.run();
            return true;
        }

        return false;
    }

    @Override
    protected void setup() {
        title = "A bank \"" + this.bank.getName() + "\"\n" +
                "Interest on balance for debit per year: " + bank.getInterestOnBalance() + "\n" +
                "Restriction for doubtful for per year: " + bank.getRestrictionForDoubtful() + "\n" +
                "Transfer's limit for questionable clients: " + bank.getLimitForQuestionable() + "\n";

        add("Create client", new CreateClientScenario(bank));
        add("Select a client", new ChooseClientScenario(bank));
    }
}
