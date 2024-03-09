package ru.mishandro.presentation;

import ru.mishandro.presentation.abstractions.ChosenScenario;
import ru.mishandro.presentation.abstractions.Scenario;
import ru.mishandro.presentation.bank.ChooseBankScenario;
import ru.mishandro.presentation.bank.CreateBankScenario;

public class MainScenario extends ChosenScenario<Scenario> implements Scenario {
    public MainScenario() {
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
        title = "Select an action:";
        add("Create bank", new CreateBankScenario());
        add("Select a bank", new ChooseBankScenario());
    }
}
