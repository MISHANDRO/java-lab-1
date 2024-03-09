package ru.mishandro.entities;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.services.BankService;
import ru.mishandro.services.builders.BankBuilder;
import ru.mishandro.services.repositories.BankRepository;

import java.util.List;

/**
 * An object dealing with the interaction between banks
 */
public class CentralBank {
    private BankRepository bankRepository;
    private BankService bankService;

    private CentralBank() {}
    private static CentralBank obj = null;

    /**
     * return an instance of an object
     */
    public static CentralBank instance() {
        if (obj == null) {
            obj = new CentralBank();
        }

        return obj;
    }

    /**
     * Initial setup
     * @param bankRepository implementation of {@link BankRepository}
     * @param bankService an object of {@link BankService}
     */
    public static void setup(
            @NotNull BankRepository bankRepository,
            @NotNull BankService bankService) {
        instance().bankRepository = bankRepository;
        instance().bankService = bankService;
    }

    /**
     * return builder for create new bank
     */
    public BankBuilder getBankBuilder() {
        return new BankBuilder(bankRepository);
    }

    /**
     * Notifies all banks that they need to charge interest on the balance/commission
     */
    public void notifyAboutAccrualOfInterestAndCommission() {
        for (Bank bank : bankRepository.getAllBanks()) {
            bankService.accrualOfInterestAndCommission(bank);
        }
    }

    /**
     * return {@link List} of all existing {@link Bank}s
     */
    public @NotNull List<Bank> getAllBanks() {
        return bankRepository.getAllBanks();
    }

    /**
     * return an object of the {@link Bank} (null - if it doesn't exist)
     * @param bankId the id of the bank that needs to be received
     */
    public Bank getBankById(int bankId) {
        return bankRepository.getBankById(bankId);
    }

    /**
     * return an object of the {@link Bank} (null - if it doesn't exist)
     * @param name the name of the bank that needs to be received
     */
    public Bank getBankByName(@NotNull String name) {
        return bankRepository.getBankByName(name);
    }

    /**
     * Refactoring {@link Bank}
     * @param bank {@link Bank} with another parameters
     */
    public void updateBankParameters(@NotNull Bank bank) {
        Bank cur = CentralBank.instance().getBankById(bank.getId());
        if (cur != null && !cur.getName().equalsIgnoreCase(bank.getName())) {
            bankRepository.update(bank);
        }

        bankService.modified(bank);
    }
}
