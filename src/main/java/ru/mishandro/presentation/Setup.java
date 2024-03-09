package ru.mishandro.presentation;

import ru.mishandro.entities.Bank;
import ru.mishandro.entities.CentralBank;
import ru.mishandro.entities.Client;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.entities.accounts.CreditAccount;
import ru.mishandro.entities.accounts.DebitAccount;
import ru.mishandro.entities.accounts.DepositAccount;
import ru.mishandro.models.BankAccountType;
import ru.mishandro.models.Time;
import ru.mishandro.models.chanes.BankAccountFactoryLinkChain;
import ru.mishandro.services.AccountService;
import ru.mishandro.services.BankService;
import ru.mishandro.services.ClientService;
import ru.mishandro.services.factories.bankaccounts.CreditAccountFactory;
import ru.mishandro.services.factories.bankaccounts.DebitAccountFactory;
import ru.mishandro.services.factories.bankaccounts.DepositAccountFactory;
import ru.mishandro.services.factories.visitor.DefaultBankAccountOperationVisitorFactory;
import ru.mishandro.services.repositories.*;

import java.time.Duration;
import java.util.Set;
import java.util.TreeMap;

public class Setup {
    private final BankRepository bankRepository = new InMemoryBankRepository();
    private final ClientRepository clientRepository = new InMemoryClientRepository();
    private final AccountRepository accountRepository = new InMemoryAccountRepository();
    private final TransactionRepository transactionRepository = new InMemoryTransactionRepository();

    private final Time time = new Time();

    private final BankService bankService;
    private final ClientService clientService;
    private final AccountService accountService;

    private Setup() {
        clientService = new ClientService(clientRepository);
        accountService = new AccountService(
                getLinkChain(time), clientService, accountRepository, transactionRepository);

        bankService = new BankService(
                clientService,
                accountService,
                new DefaultBankAccountOperationVisitorFactory(
                        time, accountService, accountRepository, transactionRepository
                )
        );

        CentralBank.setup(bankRepository, bankService);
    }

    private static Setup obj = null;

    public static Setup instance() {
        if (obj == null) {
            obj = new Setup();
        }
        return obj;
    }

    public BankService getBankService() {
        return bankService;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void createDefaultData() {
        TreeMap<Integer, Double> treeMap1 = new TreeMap<>();
        treeMap1.put(0, 3.);
        treeMap1.put(50000, 3.5);
        treeMap1.put(100000, 4.);
        Bank bank1 = new Bank(
                "FirstBank", treeMap1, 3.65, 10, 1000, Duration.ofDays(365000));

        TreeMap<Integer, Double> treeMap2 = new TreeMap<>();
        treeMap2.put(0, 6.7);
        treeMap2.put(100000, 10.3);
        Bank bank2 = new Bank(
                "SecondBank", treeMap2, 1.64, 15.34, 150000, Duration.ofDays(0));
        bankRepository.addBank(bank1);
        bankRepository.addBank(bank2);


        Client client1 = new Client("Client1", "First", "uiop", "1234 123456", bank1.getId());
        Client client2 = new Client("Client2", "Second", null, "0987 098765", bank1.getId());
        Client client3 = new Client("Client3", "Third", "qwerty", null, bank2.getId());

        clientRepository.addClient(client1);
        clientRepository.addClient(client2);
        clientRepository.addClient(client3);


        BankAccount debitAccount1 = new DebitAccount(client1.getId(), bank1.getId());
        BankAccount depositAccountOpen2 = new DepositAccount(client1.getId(), bank1.getId(), time.getCurrentDate());
        BankAccount creditAccount3 = new CreditAccount(client2.getId(), bank1.getId());
        BankAccount depositAccountClose4 = new DepositAccount(client3.getId(), bank2.getId(), time.getCurrentDate());

        accountRepository.addAccount(debitAccount1);
        accountRepository.addAccount(depositAccountOpen2);
        accountRepository.addAccount(creditAccount3);
        accountRepository.addAccount(depositAccountClose4);
    }

    private BankAccountFactoryLinkChain getLinkChain(Time time) {
        BankAccountFactoryLinkChain linkChain1 = new BankAccountFactoryLinkChain(BankAccountType.Debit, new DebitAccountFactory());
        BankAccountFactoryLinkChain linkChain2 = new BankAccountFactoryLinkChain(BankAccountType.Credit, new CreditAccountFactory());
        BankAccountFactoryLinkChain linkChain3 = new BankAccountFactoryLinkChain(BankAccountType.Deposit, new DepositAccountFactory(time));
        linkChain1.setNext(linkChain2);
        linkChain1.setNext(linkChain3);

        return linkChain1;
    }
}
