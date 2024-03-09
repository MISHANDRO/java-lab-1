package ru.mishandro;

import ru.mishandro.presentation.MainScenario;
import ru.mishandro.presentation.Setup;

public class Main {
    public static void main(String[] args) {
        Setup.instance().createDefaultData();
        MainScenario mainScenario = new MainScenario();
        mainScenario.run();
    }
}