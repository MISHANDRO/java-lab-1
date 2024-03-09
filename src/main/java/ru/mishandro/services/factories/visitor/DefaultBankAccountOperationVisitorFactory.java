package ru.mishandro.services.factories.visitor;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.models.Time;
import ru.mishandro.models.visitors.BankAccountOperationVisitor;
import ru.mishandro.models.visitors.DefaultBankAccountOperationVisitor;
import ru.mishandro.models.visitors.RepositoryUpdateAndTransactionVisitor;
import ru.mishandro.services.AccountService;
import ru.mishandro.services.repositories.AccountRepository;
import ru.mishandro.services.repositories.TransactionRepository;

public class DefaultBankAccountOperationVisitorFactory implements BankAccountOperationVisitorFactory {
    private final Time time;
    private DefaultBankAccountOperationVisitor cache;
    private final RepositoryUpdateAndTransactionVisitor main;

    private final int addRestrictionDay = 27;

    public DefaultBankAccountOperationVisitorFactory(
            @NotNull Time time,
            @NotNull AccountService accountService,
            @NotNull AccountRepository accountRepository,
            @NotNull TransactionRepository transactionRepository
    ) {
        this.time = time;
        main = new RepositoryUpdateAndTransactionVisitor(accountRepository, transactionRepository);
        cache = new DefaultBankAccountOperationVisitor(time, accountService);

        main.setVisitor(cache);
    }

    @Override
    public @NotNull BankAccountOperationVisitor create(@NotNull Bank bank) {
        cache.setBank(bank);
        cache.setAddRestrictionForDoubtful(time.getCurrentLocalDate().getDayOfMonth() == addRestrictionDay);

        return main;
    }
}
