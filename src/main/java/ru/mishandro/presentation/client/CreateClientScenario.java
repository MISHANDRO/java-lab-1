package ru.mishandro.presentation.client;

import ru.mishandro.entities.Bank;
import ru.mishandro.entities.Client;
import ru.mishandro.presentation.Setup;
import ru.mishandro.presentation.abstractions.Scenario;
import ru.mishandro.services.builders.client.ClientBuilder;
import ru.mishandro.services.builders.client.NameClientBuilder;

import java.util.Scanner;

public class CreateClientScenario implements Scenario {
    public final Bank bank;

    public CreateClientScenario(Bank bank) {
        this.bank = bank;
    }

    @Override
    public boolean handle() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter name: ");
        String name = scanner.next();

        System.out.print("Enter surname: ");
        String surname = scanner.next();

        System.out.print("Enter address (can be null): ");
        String address = scanner.next();

        System.out.print("Enter passport number (can be null): ");
        String passportNumber = scanner.next();

        Client client = Setup.instance().getClientService().getClientBuilder(bank)
                .withNameAndSurname(name, surname)
                .withAddress(address)
                .withPassportNumber(passportNumber)
                .create();

        if (client == null) {
            return false;
        }

        new ClientScenario(client).run();
        return false;
    }
}
