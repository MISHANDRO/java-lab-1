package ru.mishandro.services.repositories;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.entities.Client;

import java.util.List;

public interface ClientRepository {
    boolean addClient(@NotNull Client client);
    @NotNull List<Client> getClientsByBank(@NotNull Bank bank);
    Client getClientById(int id);
    void updateClientParameters(@NotNull Client client);
}
