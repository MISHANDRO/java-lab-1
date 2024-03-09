package ru.mishandro.entities;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.TreeMap;

/**
 * An object describing the parameters of banks
 */
public class Bank {
    private int id = -1;
    private String name;
    private TreeMap<Integer, Double> debitPercents;
    private double interestOnBalance;
    private double restrictionForDoubtful;
    private int limitForQuestionable;
    private final Duration depositActiveDuration;

    /**
     * @param name Bank's name
     * @param debitPercents percents for debit accounts: minSum - percent per year
     * @param interestOnBalance interest On balance per year
     * @param restrictionForDoubtful commission for credit bank account per year
     * @param limitForQuestionable limit for client who doesn't have address info or passport number info
     * @param depositActiveDuration operation's deposit bank account's {@link Duration} of life
     */
    public Bank(
            @NotNull String name,
            @NotNull TreeMap<Integer, Double> debitPercents,
            double interestOnBalance,
            double restrictionForDoubtful,
            int limitForQuestionable,
            @NotNull Duration depositActiveDuration
    ) {
        this.name = name;
        this.debitPercents = new TreeMap<>(debitPercents);
        this.interestOnBalance = interestOnBalance;
        this.restrictionForDoubtful = restrictionForDoubtful;
        this.limitForQuestionable = limitForQuestionable;
        this.depositActiveDuration = depositActiveDuration;
    }

    /**
     *  Copy of this {@link Bank}
     */
    public Bank clone() {
        Bank cloneBank = new Bank(
                name,
                debitPercents,
                interestOnBalance,
                restrictionForDoubtful,
                limitForQuestionable,
                depositActiveDuration
        );
        cloneBank.setId(id);

        return cloneBank;
    }

    /**
     *  Bank's id
     */
    public int getId() {
        return id;
    }

    /**
     *  Bank's name
     */
    public String getName() {
        return name;
    }

    /**
     *  percents for debit accounts: minSum - percent per year
     */
    public TreeMap<Integer, Double> getDebitPercents() {
        return new TreeMap<>(debitPercents);
    }

    /**
     *  interest on balance per year
     */
    public double getInterestOnBalance() {
        return interestOnBalance;
    }

    /**
     *  commission for credit bank account per year
     */
    public double getRestrictionForDoubtful() {
        return restrictionForDoubtful;
    }

    /**
     *  operation's limit for client who doesn't have address info
     * or passport number info
     */
    public int getLimitForQuestionable() {
        return limitForQuestionable;
    }

    /**
     *  operation's deposit bank account's {@link Duration} of life
     */
    public Duration getDepositActiveDuration() {
        return depositActiveDuration;
    }

    /**
     * Set id (only for this object!)
     * @param id new value
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set percents for debit accounts: minSum - percent per year (only for this object!)
     * @param debitPercents new values
     */
    public void setDebitPercents(TreeMap<Integer, Double> debitPercents) {
        this.debitPercents = new TreeMap<>(debitPercents);
    }

    /**
     * Set interest on balance per year (only for this object!)
     * @param interestOnBalance new value
     */
    public void setInterestOnBalance(double interestOnBalance) {
        this.interestOnBalance = interestOnBalance;
    }

    /**
     * Set commission for credit bank account per year (only for this object!)
     * @param restrictionForDoubtful new value
     */
    public void setRestrictionForDoubtful(double restrictionForDoubtful) {
        this.restrictionForDoubtful = restrictionForDoubtful;
    }

    /**
     * Set operation's limit for client who doesn't have address info or
     * passport number info (only for this object!)
     * @param limitForQuestionable new value
     */
    public void setLimitForQuestionable(int limitForQuestionable) {
        this.limitForQuestionable = limitForQuestionable;
    }
}