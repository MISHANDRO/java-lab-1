package ru.mishandro.presentation.abstractions;

public interface Scenario {
    boolean handle();
    default void run() {
        while (true) {
            System.out.flush();
            if (!handle()) {
                break;
            }
        }
    }
}
