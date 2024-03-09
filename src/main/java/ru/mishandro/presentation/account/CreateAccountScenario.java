package ru.mishandro.presentation.account;

import ru.mishandro.entities.Client;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.models.BankAccountType;
import ru.mishandro.presentation.Setup;
import ru.mishandro.presentation.abstractions.ChosenScenario;
import ru.mishandro.presentation.abstractions.Scenario;

public class CreateAccountScenario extends ChosenScenario<BankAccountType> implements Scenario {
    private final Client client;

    public CreateAccountScenario(Client client) {
        this.client = client;
    }

    @Override
    public boolean handle() {
        BankAccountType bankAccountType = read();
        if (bankAccountType == null) {
            return false;
        }

        Setup.instance().getAccountService().createBankAccountByClient(client, bankAccountType);
        return false;
    }

    @Override
    protected void setup() {
        title = "Select type of new bank account:";
        add("Debit", BankAccountType.Debit);
        add("Deposit", BankAccountType.Deposit);
        add("Credit", BankAccountType.Credit);
    }
}
