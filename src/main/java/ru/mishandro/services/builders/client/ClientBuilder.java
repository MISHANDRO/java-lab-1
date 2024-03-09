package ru.mishandro.services.builders.client;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.entities.Client;
import ru.mishandro.services.repositories.ClientRepository;

public class ClientBuilder implements NameClientBuilder, AddressPassportClientBuilder {

    private final int bankOwnerId;
    private String name;
    private String surname;
    private String address;
    private String passportNumber;

    private final ClientRepository clientRepository;

    public ClientBuilder(@NotNull Bank bankOwner, @NotNull ClientRepository clientRepository) {
        this.bankOwnerId = bankOwner.getId();
        this.clientRepository = clientRepository;
    }

    @Override
    public AddressPassportClientBuilder withNameAndSurname(@NotNull String name, @NotNull String surname) {
        this.name = name.trim();
        this.surname = surname.trim();
        return this;
    }

    @Override
    public AddressPassportClientBuilder withAddress(@NotNull String address) {
        this.address = address.trim();
        return this;
    }

    @Override
    public AddressPassportClientBuilder withPassportNumber(@NotNull String passportNumber) {
        this.passportNumber = passportNumber.trim();
        return this;
    }

    @Override
    public Client create() {
        Client newClient = new Client(name, surname, bankOwnerId);
        newClient.setAddress(address);
        newClient.setPassportNumber(passportNumber);

        if (!clientRepository.addClient(newClient)) {
            return null;
        }

        return newClient;
    }
}
