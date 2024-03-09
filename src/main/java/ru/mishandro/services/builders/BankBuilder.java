package ru.mishandro.services.builders;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.services.repositories.BankRepository;

import java.time.Duration;
import java.util.TreeMap;

public class BankBuilder {
    private String name = "";
    private final TreeMap<Integer, Double> debitPercents = new TreeMap<>();
    private double interestOnBalance = 0;
    private double restrictionForDoubtful = 0;
    private int limitForQuestionable = 0;
    private Duration depositActiveDuration = Duration.ofDays(0);

    private final BankRepository bankRepository;

    public BankBuilder(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public BankBuilder withName(@NotNull String name) {
        this.name = name.trim();
        return this;
    }

    public BankBuilder withInterestOnBalance(double interestOnBalance) {
        this.interestOnBalance = interestOnBalance;
        return this;
    }

    public BankBuilder withRestrictionForDoubtful(double restrictionForDoubtful) {
        this.restrictionForDoubtful = restrictionForDoubtful;
        return this;
    }

    public BankBuilder withLimitForQuestionable(int limitForQuestionable) {
        this.limitForQuestionable = limitForQuestionable;
        return this;
    }

    public BankBuilder withDepositActiveDuration(@NotNull Duration depositActiveDuration) {
        this.depositActiveDuration = depositActiveDuration;
        return this;
    }

    public BankBuilder addDebitPercent(int minSum, double percent) {
        this.debitPercents.put(minSum, percent);
        return this;
    }

    public Bank create() {
        if (this.name.isEmpty()) {
            return null;
        }

        Bank newBank = new Bank(
                this.name,
                this.debitPercents,
                this.interestOnBalance,
                this.restrictionForDoubtful,
                this.limitForQuestionable,
                this.depositActiveDuration
        );

        if (!this.bankRepository.addBank(newBank)) {
            return null;
        }

        return newBank;
    }
}
