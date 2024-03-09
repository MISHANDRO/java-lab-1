package ru.mishandro.presentation.abstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

abstract public class ChosenScenario<T> {
    private final List<String> listOfStrings = new ArrayList<>();
    private final List<T> listOfElements = new ArrayList<>();
    protected String title = "";

    protected final T read() {
        writeDescription();
        Scanner scanner = new Scanner(System.in);
        int index = scanner.nextInt();
        while (index <= 0 || index > listOfElements.size() + 1) {
            System.out.flush();
            writeDescription();
            System.out.println("Enter a number in the range between 1 and " + (listOfElements.size() + 1) + "!");
            index = scanner.nextInt();
        }

        if (index <= listOfElements.size()) {
            return listOfElements.get(index - 1);
        }

        return null;
    }

    protected void add(String string, T element) {
        listOfStrings.add(string);
        listOfElements.add(element);
    }

    protected void writeDescription() {
        System.out.println(title);
        for (int i = 0; i < listOfStrings.size(); ++i) {
            System.out.println("\t" + (i + 1) + ": " + listOfStrings.get(i));
        }
        System.out.println("\t" + (listOfStrings.size() + 1) + ": Back");
    }

    protected void clearLists() {
        listOfStrings.clear();
        listOfElements.clear();
    }

    protected abstract void setup();
}
