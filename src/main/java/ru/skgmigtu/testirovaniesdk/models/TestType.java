package ru.skgmigtu.testirovaniesdk.models;

public enum TestType {

    RATING_1(1),
    RATING_2(2),
    ZACHET(3),
    EXAM(4);

    private int value;

    TestType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}