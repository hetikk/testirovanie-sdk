package ru.skgmigtu.testirovaniesdk.models.enums;

public enum Part {

    A("1"),
    B("2");

    private String value;

    Part(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}