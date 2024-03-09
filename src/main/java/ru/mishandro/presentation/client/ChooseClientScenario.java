package ru.mishandro.presentation.client;

import ru.mishandro.entities.Bank;
import ru.mishandro.entities.CentralBank;
import ru.mishandro.entities.Client;
import ru.mishandro.presentation.Setup;
import ru.mishandro.presentation.abstractions.ChosenScenario;
import ru.mishandro.presentation.abstractions.Scenario;
import ru.mishandro.presentation.bank.BankScenario;

import java.util.List;

public class ChooseClientScenario extends ChosenScenario<Client> implements Scenario {
    private final Bank bank;

    public ChooseClientScenario(Bank bank) {
        this.bank = bank;
        setup();
    }

    @Override
    public boolean handle() {
        Client cur = read();
        if (cur == null) {
            return false;
        }

        Scenario newScenario = new ClientScenario(cur);
        newScenario.run();
        return true;
    }

    @Override
    protected void setup() {
        clearLists();
        List<Client> clients = Setup.instance().getClientService().getClientsByBank(bank);
        if (clients.isEmpty()) {
            title = "This bank has no clients(";
            return;
        }

        title = "Select a client of bank \"" + bank.getName() + "\":";
        for (Client client : clients) {
            add(client.getName() + " " + client.getSurname(), client);
        }
    }

    @Override
    protected void writeDescription() {
        setup();
        super.writeDescription();
    }
}
