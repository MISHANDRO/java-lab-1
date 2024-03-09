package ru.mishandro.services.factories.visitor;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.models.visitors.BankAccountOperationVisitor;

public interface BankAccountOperationVisitorFactory {
    @NotNull BankAccountOperationVisitor create(@NotNull Bank bank);
}
