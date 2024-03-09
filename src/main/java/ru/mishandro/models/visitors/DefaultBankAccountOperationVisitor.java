package ru.mishandro.models.visitors;

import org.jetbrains.annotations.NotNull;
import ru.mishandro.entities.Bank;
import ru.mishandro.entities.accounts.BankAccount;
import ru.mishandro.entities.accounts.CreditAccount;
import ru.mishandro.entities.accounts.DebitAccount;
import ru.mishandro.entities.accounts.DepositAccount;
import ru.mishandro.models.Money;
import ru.mishandro.models.Time;
import ru.mishandro.services.AccountService;

import java.util.Map;

public class DefaultBankAccountOperationVisitor implements BankAccountOperationVisitor {
    private boolean isAddRestrictionForDoubtful;
    private Bank bank;
    private final Time time;
    private final AccountService accountService;

    public DefaultBankAccountOperationVisitor(@NotNull Time time, @NotNull AccountService accountService) {
        this.time = time;
        this.accountService = accountService;
    }

    public void setAddRestrictionForDoubtful(boolean addRestrictionForDoubtful) {
        isAddRestrictionForDoubtful = addRestrictionForDoubtful;
    }

    public void setBank(@NotNull Bank bank) {
        this.bank = bank;
    }

    @Override
    public boolean debitMakeTransfer(@NotNull DebitAccount accountFrom, @NotNull BankAccount accountTo, @NotNull Money amount) {
        if (!checkLimit(accountFrom, amount) || !debitPerformOperation(accountFrom, amount.makeMinus())) {
            return false;
        }

        accountTo.getBalance().add(amount.makePositive());
        return true;
    }

    @Override
    public boolean depositMakeTransfer(@NotNull DepositAccount accountFrom, @NotNull BankAccount accountTo, @NotNull Money amount) {
        if (!checkLimit(accountFrom, amount) || !depositPerformOperation(accountFrom, amount.makeMinus())) {
            return false;
        }

        accountTo.getBalance().add(amount.makePositive());
        return true;
    }

    @Override
    public boolean creditMakeTransfer(@NotNull CreditAccount accountFrom, @NotNull BankAccount accountTo, @NotNull Money amount) {
        if (!checkLimit(accountFrom, amount) || !creditPerformOperation(accountFrom, amount.makePositive())) {
            return false;
        }

        accountTo.getBalance().add(amount.makePositive());
        return true;
    }

    @Override
    public boolean debitPerformOperation(@NotNull DebitAccount account, @NotNull Money amount) {
        if (amount.moreOrEquipThan(0)) {
            account.getBalance().add(amount);
            return true;
        }

        amount = amount.makePositive();
        if (account.getBalance().lessThan(amount) || !checkLimit(account, amount)) {
            return false;
        }

        account.getBalance().subtract(amount);
        return true;
    }

    @Override
    public boolean depositPerformOperation(@NotNull DepositAccount account, @NotNull Money amount) {
        if (amount.moreOrEquipThan(0)) {
            account.getBalance().add(amount);
            return true;
        }

        amount = amount.makePositive();
        if (depositIsOpen(account) || account.getBalance().lessThan(amount) || !checkLimit(account, amount)) {
            return false;
        }

        account.getBalance().subtract(amount);
        return true;
    }

    @Override
    public boolean creditPerformOperation(@NotNull CreditAccount account, @NotNull Money amount) {
        account.getBalance().add(amount);

        if (amount.lessThan(0) && !checkLimit(account, account.getBalance().makePositive())) {
            account.getBalance().subtract(amount);
            return false;
        }

        return true;
    }

    @Override
    public Money accrualInterestOnBalanceAndDeductionOfCommission(@NotNull DebitAccount account) {
        Money result = Money.multiply(account.getBalance(), bank.getInterestOnBalance() / 365);

        account.getInterestOnBalance().add(result);
        if (isAddRestrictionForDoubtful) {
            account.getBalance().add(account.getInterestOnBalance());
            account.getInterestOnBalance().setValue(0);

            return result;
        }

        return null;
    }

    @Override
    public Money accrualInterestOnBalanceAndDeductionOfCommission(@NotNull DepositAccount account) {
        if (!depositIsOpen(account)) {
            return null;
        }

        Double debitPercent = bank.getDebitPercents().firstEntry().getValue() / 365;
        for (Map.Entry<Integer, Double> entry : bank.getDebitPercents().entrySet()) {
            if (account.getBalance().lessThan(entry.getKey())) {
                break;
            }

            debitPercent = entry.getValue() / 365;
        }

        Money result = Money.multiply(account.getBalance(), debitPercent);
        account.getInterestOnBalance().add(result);
        if (isAddRestrictionForDoubtful) {
            account.getBalance().add(account.getInterestOnBalance());
            account.getInterestOnBalance().setValue(0);
            return result;
        }

        return null;
    }

    @Override
    public Money accrualInterestOnBalanceAndDeductionOfCommission(@NotNull CreditAccount account) {
        if (account.getBalance().moreOrEquipThan(0)) {
            return null;
        }

        Money result = Money.multiply(account.getBalance(), bank.getRestrictionForDoubtful() / 365);
        account.getBalance().add(result);

        return result;
    }

    private boolean checkLimit(@NotNull BankAccount bankAccount, @NotNull Money amount) {
        if (!accountService.accountIsQuestionable(bankAccount)) {
            return true;
        }

        return amount.lessOrEquipThan(bank.getLimitForQuestionable());
    }

    private boolean depositIsOpen(@NotNull DepositAccount depositAccount) {
        return time.getCurrentDate().getTime() / 1000 <
                depositAccount.getOpenDate().getTime() / 1000 + bank.getDepositActiveDuration().getSeconds();
    }
}
