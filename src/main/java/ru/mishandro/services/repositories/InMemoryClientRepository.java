package ru.mishandro.services.repositories;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.entities.Client;

import java.util.ArrayList;
import java.util.List;

public class InMemoryClientRepository implements ClientRepository {
    private final List<Client> clients = new ArrayList<>();

    @Override
    public boolean addClient(@NotNull Client client) {
        client.setId(clients.size() + 1);
        clients.add(client);

        return true;
    }

    @Override
    public @NotNull List<Client> getClientsByBank(@NotNull Bank bank) {
        return clients.stream()
                .filter(client -> client.getBankId() == bank.getId())
                .map(Client::clone)
                .toList();
    }

    @Override
    public Client getClientById(int id) {
        return clients.stream()
                .filter(client -> client.getId() == id)
                .findFirst()
                .map(Client::clone)
                .orElse(null);
    }
}
