package ru.mishandro;

import ru.mishandro.entities.Bank;
import ru.mishandro.entities.CentralBank;
import ru.mishandro.entities.Client;
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
import java.util.Date;
import java.util.TreeMap;

public class Setup {
    private BankRepository bankRepository = new InMemoryBankRepository();
    private ClientRepository clientRepository = new InMemoryClientRepository();
    private AccountRepository accountRepository = new InMemoryAccountRepository();
    private TransactionRepository transactionRepository = new InMemoryTransactionRepository();


    public Bank bank1;
    public Bank bank2;

    public Client client1;
    public Client client2;
    public Client client3;

    public DebitAccount debitAccount1;
    public DepositAccount depositAccountOpen2;
    public CreditAccount creditAccount3;
    public DepositAccount depositAccountClose4;


    public void setupCentralBank(BankService bankService) {
        CentralBank.setup(bankRepository, bankService);
    }

    public BankService getBankService(ClientService clientService, AccountService accountService, Time time) {
        return new BankService(
                clientService,
                accountService,
                new DefaultBankAccountOperationVisitorFactory(
                        time, accountService, accountRepository, transactionRepository
                )
        );
    }

    public ClientService getClientService() {
        return new ClientService(clientRepository);
    }

    public AccountService getAccountService(ClientService clientService, Time time) {
        return new AccountService(getLinkChain(time), clientService, accountRepository, transactionRepository);

    }

    public void createTestData(Time time) {
        TreeMap<Integer, Double> treeMap1 = new TreeMap<>();
        treeMap1.put(0, 3.);
        treeMap1.put(50000, 3.5);
        treeMap1.put(100000, 4.);
        bank1 = new Bank(
                "FirstBank", treeMap1, 3.65, 10, 1000, Duration.ofDays(365000));

        TreeMap<Integer, Double> treeMap2 = new TreeMap<>();
        treeMap2.put(0, 6.7);
        treeMap2.put(100000, 10.3);
        bank2 = new Bank(
                "SecondBank", treeMap2, 1.64, 15.34, 150000, Duration.ofDays(0));
        bankRepository.addBank(bank1);
        bankRepository.addBank(bank2);

        Client client1 = new Client("Client1", "First", bank1.getId());
        client1.setAddress("uiop");
        client1.setPassportNumber("1234 123456");
        Client client2 = new Client("Client2", "Second", bank1.getId());
        client2.setPassportNumber("0987 098765");
        Client client3 = new Client("Client3", "Third", bank2.getId());
        client3.setAddress("qwerty");

        clientRepository.addClient(client1);
        clientRepository.addClient(client2);
        clientRepository.addClient(client3);


        debitAccount1 = new DebitAccount(client1.getId(), bank1.getId());
        depositAccountOpen2 = new DepositAccount(client1.getId(), bank1.getId(), time.getCurrentDate());
        creditAccount3 = new CreditAccount(client2.getId(), bank1.getId());
        depositAccountClose4 = new DepositAccount(client3.getId(), bank2.getId(), time.getCurrentDate());

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
