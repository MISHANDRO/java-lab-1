package ru.mishandro.services.builders.client;

import org.jetbrains.annotations.NotNull;

public interface NameClientBuilder {
    AddressPassportClientBuilder withNameAndSurname(@NotNull String name, @NotNull String surname);
}
