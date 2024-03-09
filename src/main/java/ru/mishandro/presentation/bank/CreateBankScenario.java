package ru.mishandro.presentation.bank;

import ru.mishandro.entities.Bank;
import ru.mishandro.entities.CentralBank;
import ru.mishandro.presentation.abstractions.Scenario;
import ru.mishandro.services.builders.BankBuilder;

import java.time.Duration;
import java.util.Scanner;

public class CreateBankScenario implements Scenario {

    @Override
    public boolean handle() {
        BankBuilder bankBuilder = CentralBank.instance().getBankBuilder();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter name: ");
        bankBuilder.withName(scanner.next());

        System.out.print("Enter interest on balance for debit per year: ");
        bankBuilder.withInterestOnBalance(scanner.nextDouble());

        System.out.print("Enter restriction for doubtful for per year: ");
        bankBuilder.withRestrictionForDoubtful(scanner.nextDouble());

        System.out.print("Enter transfer's limit for questionable clients: ");
        bankBuilder.withLimitForQuestionable(scanner.nextInt());

        System.out.print("Enter deposit's lifetime in days: ");
        bankBuilder.withDepositActiveDuration(Duration.ofDays(scanner.nextLong()));

        Bank bank = bankBuilder.create();
        if (bank == null) {
            return false;
        }

        new BankScenario(bank).run();
        return false;
    }
}
