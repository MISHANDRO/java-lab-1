package ru.mishandro.presentation.account;

import ru.mishandro.entities.Bank;
import ru.mishandro.entities.Client;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.presentation.Setup;
import ru.mishandro.presentation.abstractions.ChosenScenario;
import ru.mishandro.presentation.abstractions.Scenario;
import ru.mishandro.presentation.client.ClientScenario;

import java.util.List;

public class ChooseAccountScenario extends ChosenScenario<BankAccount> implements Scenario {
    private final Client client;

    public ChooseAccountScenario(Client client) {
        this.client = client;
        setup();
    }

    @Override
    public boolean handle() {
        BankAccount cur = read();
        if (cur == null) {
            return false;
        }

        Scenario newScenario = new AccountScenario(cur);
        newScenario.run();
        return true;
    }

    @Override
    protected void setup() {
        clearLists();
        List<BankAccount> bankAccounts = Setup.instance().getAccountService().getAccountsByClient(client);
        title = String.format("Client %s %s (%d)\n", client.getName(), client.getSurname(), client.getId());

        if (bankAccounts.isEmpty()) {
            title += "This client has no accounts(";
            return;
        }

        title += "Select an action:\n";
        for (BankAccount bankAccount : bankAccounts) {
            add("(" + bankAccount.getId() + ") - " + bankAccount.getClass().getTypeName(), bankAccount);
        }
    }

    @Override
    protected void writeDescription() {
        setup();
        super.writeDescription();
    }
}
