package ru.mishandro.models.observers;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.entities.Client;

public interface Observer {
    void onNext(@NotNull Client client, @NotNull Bank bank);
}
