package ru.skgmigtu.testirovaniesdk.models;

public enum Part {

    A(1),
    B(2);

    private int value;

    Part(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}