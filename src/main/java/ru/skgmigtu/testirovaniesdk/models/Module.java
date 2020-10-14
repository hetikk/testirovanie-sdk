package ru.skgmigtu.testirovaniesdk.models;

public enum Module {

    RATING_1(1),
    RATING_2(2),
    ZACHET(3),
    EXAM(4),
    RESIDUAL_KNOWLEDGE(6); // проверка остаточных знаний

    private int value;

    Module(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}