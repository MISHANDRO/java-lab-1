package ru.mishandro.models;

import org.jetbrains.annotations.NotNull;

public class Money {
    int value = 0;

    public Money() {}

    public Money(double value) {
        this.value = (int) (value * 100);
    }

    public Money(int value) {
        this.value = value * 100;
    }

    public Money(@NotNull Money other) {
        this.value = other.value;
    }

    public Money makeMinus() {
        Money money = new Money();
        money.value = -Math.abs(this.value);
        return money;
    }

    public Money makePositive() {
        Money money = new Money();
        money.value = Math.abs(this.value);
        return money;
    }

    public Money makeInvert() {
        Money money = new Money();
        money.value = -this.value;
        return money;
    }

    public boolean isNull() {
        return value == 0;
    }

    public int getInt() {
        return value / 100;
    }

    public double getDouble() {
        return value / 100.0;
    }

    public void setValue(double val) {
        this.value = (int) (val * 100);
    }

    public void setValue(@NotNull Money money) {
        this.value = money.value;
    }

    public Money add(@NotNull Money money) {
        this.value += money.value;
        return this;
    }

    public Money add(double val) {
        this.value += (int) (val * 100);
        return this;
    }

    public Money subtract(@NotNull Money money) {
        this.value -= money.value;
        return this;
    }

    public Money subtract(double val) {
        this.value -= (int) (val * 100);
        return this;
    }

    public Money multiply(double val) {
        this.value = (int) (this.value * val);
        return this;
    }

    public Money divide(double val) {
        this.value = (int) (this.value / val);
        return this;
    }

    public boolean lessThan(double val) {
        return this.value < val * 100;
    }

    public boolean lessThan(@NotNull Money other) {
        return this.value < other.value;
    }

    public boolean lessOrEquipThan(double val) {
        return this.value <= val * 100;
    }

    public boolean moreThan(double val) {
        return this.value > val * 100;
    }

    public boolean moreOrEquipThan(double val) {
        return this.value >= val * 100;
    }

    public static @NotNull Money multiply(@NotNull Money money, double val) {
        Money result = new Money();
        result.value = (int) (money.value * val / 100);

        return result;
    }
}
