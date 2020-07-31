package ru.skgmigtu.testirovaniesdk.models;

public enum Type {

    RATING_1("1"),
    RATING_2("2"),
    ZACHET("3"),
    EXAM("4");

    private String value;

    Type(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}