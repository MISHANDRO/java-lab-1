package ru.mishandro.services.builders.client;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Client;

public interface AddressPassportClientBuilder {
    AddressPassportClientBuilder withAddress(@NotNull String address);
    AddressPassportClientBuilder withPassportNumber(@NotNull String passportNumber);
    Client create();
}
