package ru.mishandro.presentation.client;

import ru.mishandro.entities.Client;
import ru.mishandro.presentation.abstractions.ChosenScenario;
import ru.mishandro.presentation.abstractions.Scenario;
import ru.mishandro.presentation.account.ChooseAccountScenario;
import ru.mishandro.presentation.account.CreateAccountScenario;
import ru.mishandro.presentation.bank.ChooseBankScenario;

public class ClientScenario extends ChosenScenario<Scenario> implements Scenario {
    private final Client client;

    public ClientScenario(Client client) {
        this.client = client;
    }

    @Override
    protected void setup() {
        StringBuilder builder = new StringBuilder(
                String.format("Client %s %s (%d)", client.getName(), client.getSurname(), client.getId())
        ).append('\n')
                .append("Passport: ").append(client.getPassportNumber()).append('\n')
                .append("Address: ").append(client.getAddress()).append('\n').append('\n');

        builder.append("Select an action:\n");
        title = builder.toString();

        add("Create a bank account", new CreateAccountScenario(client));
        add("Select a bank account", new ChooseAccountScenario(client));
        add("Set parameters",        new SetClientScenario(client));
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
}
