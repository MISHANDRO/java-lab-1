package ru.mishandro.presentation.account;

import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.models.Money;
import ru.mishandro.presentation.Setup;
import ru.mishandro.presentation.abstractions.ChosenScenario;
import ru.mishandro.presentation.abstractions.Scenario;
import ru.mishandro.presentation.enums.AccountAction;

import java.util.Scanner;

public class AccountScenario extends ChosenScenario<AccountAction> implements Scenario {
    private final BankAccount bankAccount;
    private Boolean prevResult = null;

    public AccountScenario(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    protected void setup() {
        title = "Bank account parameters:\n" +
                "Id: " + bankAccount.getId() + '\n' +
                "Type: " + bankAccount.getClass().getTypeName() + '\n' +
                "Balance: " + bankAccount.getBalance().getDouble() + '\n';

        add("Transfer", AccountAction.Transfer);
        add("Deposit / Withdraw", AccountAction.Operation);
    }

    @Override
    public boolean handle() {
        if (prevResult != null) {
            System.out.println("Result: " + ((prevResult) ? "SUCCESS" : "UNsuccess"));
        }

        AccountAction accountAction = read();
        if (accountAction == null) {
            return false;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();

        switch (accountAction) {
            case Transfer -> {
                System.out.print("Enter recipient account ID: ");
                int id = scanner.nextInt();
                BankAccount bankAccountTo = Setup.instance().getAccountService().getAccountById(id);
                if (bankAccountTo == null) {
                    prevResult = false;
                    break;
                }

                prevResult = Setup.instance().getBankService()
                        .makeTransfer(bankAccount, bankAccountTo, new Money(amount));
            }
            case Operation -> {
                prevResult = Setup.instance().getBankService().performOperationWithAccount(bankAccount, new Money(amount));
            }
        }

        return true;
    }
}
