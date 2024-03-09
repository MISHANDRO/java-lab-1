package ru.mishandro.presentation.client;

import ru.mishandro.entities.Client;
import ru.mishandro.presentation.Setup;
import ru.mishandro.presentation.abstractions.ChosenScenario;
import ru.mishandro.presentation.abstractions.Scenario;
import ru.mishandro.presentation.enums.ClientSet;

import java.util.Scanner;

public class SetClientScenario extends ChosenScenario<ClientSet> implements Scenario {
    private final Client client;

    public SetClientScenario(Client client) {
        this.client = client;
    }

    @Override
    protected void setup() {
        title = "Choose a parameter for set:";

        add("Address", ClientSet.Address);
        add("Passport Number", ClientSet.PassportNumber);
    }

    @Override
    public boolean handle() {
        ClientSet choose = read();
        if (choose == null) {
            return false;
        }

        System.out.print("Enter new value: ");
        Scanner scanner = new Scanner(System.in);
        String newValue = scanner.nextLine();

        switch (choose) {
            case Address -> client.setAddress(newValue);
            case PassportNumber -> client.setPassportNumber(newValue);
        }

        Setup.instance().getClientService().updateClientParameters(client);
        return true;
    }
}
