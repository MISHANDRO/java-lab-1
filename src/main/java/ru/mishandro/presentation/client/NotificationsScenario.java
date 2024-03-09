package ru.mishandro.presentation.client;

import ru.mishandro.entities.Client;
import ru.mishandro.presentation.abstractions.ChosenScenario;
import ru.mishandro.presentation.abstractions.Scenario;

public class NotificationsScenario extends ChosenScenario<Notification> implements Scenario {
    private final Client client;

    public NotificationsScenario(Client client) {
        this.client = client;
    }

    @Override
    protected void setup() {

    }

    @Override
    public boolean handle() {
        return false;
    }
}
