package ru.mishandro.services;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.entities.Client;
import ru.mishandro.services.builders.client.ClientBuilder;
import ru.mishandro.services.builders.client.NameClientBuilder;
import ru.mishandro.services.repositories.ClientRepository;

import java.util.List;

public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(
            @NotNull ClientRepository clientRepository
    ) {
        this.clientRepository = clientRepository;
    }

    public NameClientBuilder getClientBuilder(@NotNull Bank bankOwner) {
        return new ClientBuilder(bankOwner, clientRepository);
    }

    public @NotNull List<Client> getClientsByBank(@NotNull Bank bank) {
        return clientRepository.getClientsByBank(bank);
    }

    public Client getClientById(int id) {
        return clientRepository.getClientById(id);
    }
}
