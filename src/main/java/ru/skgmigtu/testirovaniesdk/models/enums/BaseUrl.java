package ru.skgmigtu.testirovaniesdk.models.enums;

public enum BaseUrl {

    LOCAL("testirovanie"),
    REMOTE("http://testirovanie.skgmi-gtu.ru");

    private String value;

    BaseUrl(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}