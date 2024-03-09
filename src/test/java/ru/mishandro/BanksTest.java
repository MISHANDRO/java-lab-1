package ru.mishandro;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.mishandro.entities.Bank;
import ru.mishandro.entities.CentralBank;
import ru.mishandro.entities.Client;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.entities.accounts.CreditAccount;
import ru.mishandro.entities.accounts.DebitAccount;
import ru.mishandro.entities.accounts.DepositAccount;
import ru.mishandro.models.BankAccountType;
import ru.mishandro.models.Money;
import ru.mishandro.models.Time;
import ru.mishandro.models.Transaction;
import ru.mishandro.services.AccountService;
import ru.mishandro.services.BankService;
import ru.mishandro.services.ClientService;

import java.time.Duration;

public class BanksTest {
    private static double eps = 1e9;

    private Time time = new Time();
    private Setup setup = new Setup();

    private final BankService bankService;
    private final ClientService clientService;
    private final AccountService accountService;

    public BanksTest() {
        clientService = setup.getClientService();
        accountService = setup.getAccountService(clientService, time);
        bankService = setup.getBankService(clientService, accountService, time);

        setup.setupCentralBank(bankService);
        setup.createTestData(time);
    }

    @Test
    public void createBank() {
        String newBankName = "createBankTest";
        Assertions.assertNull(CentralBank.instance().getBankByName(newBankName));

        Bank bank = CentralBank.instance().getBankBuilder()
                .withName(newBankName)
                .withDepositActiveDuration(Duration.ofDays(456))
                .withRestrictionForDoubtful(1)
                .create();

        Assertions.assertNotNull(bank);
        Assertions.assertNotNull(CentralBank.instance().getBankByName(newBankName));
        Assertions.assertNotNull(CentralBank.instance().getBankById(bank.getId()));
    }

    @Test
    void createClient() {
        Client client = clientService.getClientBuilder(setup.bank1)
                .withNameAndSurname("Name", "Surname")
                .create();

        Assertions.assertNotNull(client);
        Assertions.assertNotNull(clientService.getClientById(client.getId()));
    }

    @Test
    void createBankAccount() {
        BankAccount creditBankAccount = accountService.createBankAccountByClient(setup.client3, BankAccountType.Credit);
        BankAccount debitBankAccount = accountService.createBankAccountByClient(setup.client3, BankAccountType.Debit);
        BankAccount depositBankAccount = accountService.createBankAccountByClient(setup.client3, BankAccountType.Deposit);

        Assertions.assertInstanceOf(CreditAccount.class, creditBankAccount);
        Assertions.assertInstanceOf(DebitAccount.class, debitBankAccount);
        Assertions.assertInstanceOf(DepositAccount.class, depositBankAccount);

        Assertions.assertNotNull(accountService.getAccountById(creditBankAccount.getId()));
        Assertions.assertNotNull(accountService.getAccountById(debitBankAccount.getId()));
        Assertions.assertNotNull(accountService.getAccountById(depositBankAccount.getId()));
    }

    @ParameterizedTest
    @CsvSource(value = {
        "0, 0, false",
        "100.23, 0, false",
        "100.23, 123, true",
        "100.23, 123.76, true",
        "100.24, -100.24, true",
        "100.24, -100.23, true",
        "100.24, -100.25, true",
        "1000, -1010, true",
        "1000, -10, true",
        "1000, -2001, false", // limit 1000 and questionable
    })
    void creditOperations(double startBalance, double amount, boolean expectedResult) {
        BankAccount curAccount = setStartBalance(setup.creditAccount3, startBalance);

        boolean result = perform(curAccount.clone(), amount);

        Assertions.assertEquals(expectedResult, result);
        curAccount = updateBankAccount(curAccount);
        double expectedBalance = startBalance - amount;
        if (expectedBalance < 0) {
            Assertions.assertTrue(startBalance - curAccount.getBalance().getDouble() < eps);
        } else {
            Assertions.assertTrue(expectedBalance - curAccount.getBalance().getDouble() < eps);
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
        "0, 0, false",
        "100.23, 0, false",
        "100.23, 123, true",
        "100.23, 123.76, true",
        "100.24, -100.24, true",
        "100.24, -100.23, true",
        "100.24, -100.25, false",
        "1000, -1010, false",
        "1000, -10, true",
    })
    void depositCloseOperations(double startBalance, double amount, boolean expectedResult) {
        BankAccount curAccount = setStartBalance(setup.depositAccountClose4, startBalance);

        boolean result = perform(curAccount.clone(), amount);

        Assertions.assertEquals(expectedResult, result);
        curAccount = updateBankAccount(curAccount);
        double expectedBalance = startBalance - amount;
        if (expectedBalance < 0) {
            Assertions.assertTrue(startBalance - curAccount.getBalance().getDouble() < eps);
        } else {
            Assertions.assertTrue(expectedBalance - curAccount.getBalance().getDouble() < eps);
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
        "0, 0, false",
        "100.23, 0, false",
        "100.23, 123, true",
        "100.23, 123.76, true",
        "100.24, -100.24, false",
        "100.24, -100.23, false",
        "100.24, -100.25, false",
        "1000, -1010, false",
        "1000, -10, false",
    })
    void depositOpenOperations(double startBalance, double amount, boolean expectedResult) {
        BankAccount curAccount = setStartBalance(setup.depositAccountOpen2, startBalance);

        boolean result = perform(curAccount, amount);

        Assertions.assertEquals(expectedResult, result);
        curAccount = updateBankAccount(curAccount);
        double expectedBalance = startBalance - amount;
        if (expectedBalance < 0) {
            Assertions.assertTrue(startBalance - curAccount.getBalance().getDouble() < eps);
        } else {
            Assertions.assertTrue(expectedBalance - curAccount.getBalance().getDouble() < eps);
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
        "0, 0, false",
        "100.23, 0, false",
        "100.23, 123, true",
        "100.23, 123.76, true",
        "100.24, -100.24, true",
        "100.24, -100.23, true",
        "100.24, -100.25, false",
        "1000, -1010, false",
        "1000, -10, true",
    })
    void debitOperations(double startBalance, double amount, boolean expectedResult) {
        BankAccount curAccount = setStartBalance(setup.debitAccount1, startBalance);

        boolean result = perform(curAccount, amount);

        Assertions.assertEquals(expectedResult, result);
        double expectedBalance = startBalance - amount;
        if (expectedBalance < 0) {
            Assertions.assertTrue(startBalance - curAccount.getBalance().getDouble() < eps);
        } else {
            Assertions.assertTrue(expectedBalance - curAccount.getBalance().getDouble() < eps);
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
            "100, 0, 100",
            "0, 0, 100",
            "0, 100, 100",
            "123.45, 567.89, 34.56",
    })
    void transfer(double startBalance1, double startBalance2, double amount) {
        BankAccount bankAccountFrom = setStartBalance(setup.creditAccount3, startBalance1);
        BankAccount bankAccountTo = setStartBalance(setup.debitAccount1, startBalance2);

        Assertions.assertTrue(bankService.makeTransfer(bankAccountFrom, bankAccountTo, new Money(amount)));

        bankAccountFrom = updateBankAccount(bankAccountFrom);
        bankAccountTo = updateBankAccount(bankAccountTo);

        Assertions.assertTrue(Math.abs(
                bankAccountFrom.getBalance().getDouble() - (startBalance1 - amount)
        ) < eps);
        Assertions.assertTrue(Math.abs(
                bankAccountTo.getBalance().getDouble() - (startBalance2 + amount)
        ) < eps);
    }

    @Test
    void transferSame() {
        BankAccount bankAccountFrom = setup.creditAccount3;
        BankAccount bankAccountTo = setup.creditAccount3;
        bankAccountFrom.getBalance().setValue(100);

        Assertions.assertFalse(bankService.makeTransfer(bankAccountFrom, bankAccountTo, new Money(10)));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "100, 0, 100",
            "1230.45, 567.89, 34.56",
    })
    void rollback(double startBalance1, double startBalance2, double amount) {
        BankAccount bankAccountFrom = setStartBalance(setup.creditAccount3, startBalance1);
        BankAccount bankAccountTo = setStartBalance(setup.debitAccount1, startBalance2);

        Assertions.assertTrue(bankService.makeTransfer(bankAccountFrom, bankAccountTo, new Money(amount)));

        bankAccountFrom = updateBankAccount(bankAccountFrom);
        bankAccountTo = updateBankAccount(bankAccountTo);

        Assertions.assertTrue(Math.abs(
                bankAccountFrom.getBalance().getDouble() - (startBalance1 - amount)
        ) < eps);
        Assertions.assertTrue(Math.abs(
                bankAccountTo.getBalance().getDouble() - (startBalance2 + amount)
        ) < eps);

        Transaction transaction = accountService.getTransactionsByAccount(bankAccountFrom.clone()).getLast();
        Assertions.assertTrue(accountService.rollbackTransaction(transaction));

        bankAccountFrom = updateBankAccount(bankAccountFrom);
        bankAccountTo = updateBankAccount(bankAccountTo);

        Assertions.assertTrue(Math.abs(
                bankAccountFrom.getBalance().getDouble() - startBalance1
        ) < eps);
        Assertions.assertTrue(Math.abs(
                bankAccountTo.getBalance().getDouble() - startBalance2
        ) < eps);
    }

    @Test
    void debitInterestOnBalance() {
        while (time.getCurrentLocalDate().getDayOfMonth() > 20) {
            time.plusDay();
        }

        BankAccount debitBankAccount = setStartBalance(setup.debitAccount1, 0);
        Money curInterestOnBalance = new Money();
        double percentDay = setup.bank1.getInterestOnBalance() / 365 / 100;

        perform(debitBankAccount, 100000); // 100000
        time.plusDay(); // + 10
        curInterestOnBalance.add(100000 * percentDay);

        perform(debitBankAccount, 100000); // 200000
        time.plusDay(); // + 20 = 30
        curInterestOnBalance.add(200000 * percentDay);

        perform(debitBankAccount, -150000); // 50000
        time.plusDay(); // + 5 = 35
        curInterestOnBalance.add(50000 * percentDay);

        perform(debitBankAccount, -50000); // 0
        time.plusDays(31);

        Assertions.assertEquals(curInterestOnBalance.getInt(), updateBankAccount(debitBankAccount).getBalance().getInt());
    }

    @Test
    void depositOpenInterestOnBalance() {
        // settings bank: 0 - 50000: 3%; 50000 - 100000: 3.5%; 100000 - ...: 4%
        while (time.getCurrentLocalDate().getDayOfMonth() != 24) {
            time.plusDay();
        }
        BankAccount depositBankAccount = setStartBalance(setup.depositAccountOpen2, 0);
        Money curInterestOnBalance = new Money();

        perform(depositBankAccount, 25000); // 25000
        time.plusDay();
        curInterestOnBalance.add(25000 * .03 / 365);

        perform(depositBankAccount, 50000); // 75000
        time.plusDay();
        curInterestOnBalance.add(75000 * .035 / 365);

        perform(depositBankAccount, 50000); // 125000
        time.plusDay();
        curInterestOnBalance.add(125000 * .04 / 365);

        Assertions.assertEquals(curInterestOnBalance.getInt(), updateBankAccount(depositBankAccount).getBalance().getInt() - 125000);
    }

    @Test
    void depositCloseInterestOnBalance() {
        BankAccount depositBankAccount = setStartBalance(setup.depositAccountClose4, 10000);
        time.plusDays(93);

        Assertions.assertEquals(10000,
                updateBankAccount(depositBankAccount).getBalance().getInt());
    }

    @Test
    void creditCommission() {
        BankAccount creditAccount = setStartBalance(setup.creditAccount3, 0);
        Money curBalance = new Money();
        double curCommissionDay = setup.bank1.getRestrictionForDoubtful() / 365 / 100;

        perform(creditAccount, -100); curBalance.subtract(100);
        time.plusDay();
        curBalance.multiply(1 + curCommissionDay);

        Assertions.assertEquals(curBalance.getInt(),
                updateBankAccount(creditAccount).getBalance().getInt());

        perform(creditAccount, -456); curBalance.subtract(456);
        time.plusDay();
        curBalance.multiply(1 + curCommissionDay);

        Assertions.assertEquals(curBalance.getInt(),
                updateBankAccount(creditAccount).getBalance().getInt());

        perform(creditAccount, 10000); curBalance.add(10000);
        time.plusDay(); // without commission

        Assertions.assertEquals(curBalance.getInt(),
                updateBankAccount(creditAccount).getBalance().getInt());
    }
    
    private BankAccount setStartBalance(BankAccount bankAccount, double startBalance) {
        BankAccount curAccount = accountService.getAccountById(bankAccount.getId());
        bankService.performOperationWithAccount(curAccount, curAccount.getBalance().makeInvert().add(startBalance));

        return curAccount;
    }

    private BankAccount updateBankAccount(BankAccount bankAccount) {
        return accountService.getAccountById(bankAccount.getId());
    }

    private boolean perform(BankAccount bankAccount, double amount) {
        return bankService.performOperationWithAccount(bankAccount, new Money(amount));
    }
}
