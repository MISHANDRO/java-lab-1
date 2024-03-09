package ru.mishandro.presentation.bank;

import ru.mishandro.entities.Bank;
import ru.mishandro.entities.CentralBank;
import ru.mishandro.presentation.abstractions.ChosenScenario;
import ru.mishandro.presentation.abstractions.Scenario;

import java.util.List;

public class ChooseBankScenario extends ChosenScenario<Bank> implements Scenario {
    public ChooseBankScenario() {
        setup();
    }

    @Override
    public boolean handle() {
        Bank cur = read();
        if (cur == null) {
            return false;
        }

        Scenario newScenario = new BankScenario(cur);
        newScenario.run();
        return true;
    }

    @Override
    protected void setup() {
        clearLists();
        List<Bank> banks = CentralBank.instance().getAllBanks();
        if (banks.isEmpty()) {
            title = "There are no banks yet(";
            return;
        }

        title = "Select a bank:";
        for (Bank bank : banks) {
            add(bank.getName(), bank);
        }
    }

    @Override
    protected void writeDescription() {
        setup();
        super.writeDescription();
    }
}
