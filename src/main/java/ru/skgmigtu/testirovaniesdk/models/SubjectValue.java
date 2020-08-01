package ru.skgmigtu.testirovaniesdk.models;

import java.io.Serializable;
import java.util.Objects;

public class SubjectValue implements Serializable, Comparable<SubjectValue> {

    private String subject;
    private int value;

    public SubjectValue() {
    }

    public SubjectValue(String subject, int value) {
        this.subject = subject;
        this.value = value;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(SubjectValue o) {
        return subject.compareTo(o.subject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectValue that = (SubjectValue) o;
        return value == that.value &&
                subject.equals(that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, value);
    }

    @Override
    public String toString() {
        return String.format("%s (value=%d)", subject, value);
    }

}
