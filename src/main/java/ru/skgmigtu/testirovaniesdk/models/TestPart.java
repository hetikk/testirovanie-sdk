package ru.skgmigtu.testirovaniesdk.models;

public enum TestPart {

    A(1),
    B(2);

    private int value;

    TestPart(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}