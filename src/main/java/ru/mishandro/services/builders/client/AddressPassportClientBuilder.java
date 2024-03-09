package ru.mishandro.services.builders.client;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Client;

public interface AddressPassportClientBuilder {
    AddressPassportClientBuilder withAddress(String address);
    AddressPassportClientBuilder withPassportNumber(String passportNumber);
    Client create();
}
