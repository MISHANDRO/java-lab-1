package ru.mishandro.models;

import ru.mishandro.entities.CentralBank;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Time {
    private final Date currentDate = new Date(0);

    public Date getCurrentDate() {
        return (Date) currentDate.clone();
    }

    public LocalDate getCurrentLocalDate() {
        return currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void plusDays(int daysToAdd) {
        if (daysToAdd <= 0) {
            return;
        }

        for (int i = 0; i < daysToAdd; ++i) {
            currentDate.setTime(currentDate.getTime() + 24L * 60 * 60 * 1000);
            CentralBank.instance().notifyAboutAccrualOfInterestAndCommission();
        }
    }

    public void plusDay() {
        plusDays(1);
    }
}
