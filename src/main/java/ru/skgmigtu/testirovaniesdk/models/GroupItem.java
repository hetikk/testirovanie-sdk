package ru.skgmigtu.testirovaniesdk.models;

import java.io.Serializable;
import java.util.Objects;

public class GroupItem implements Serializable, Comparable<GroupItem> {

    private TestType testType;
    private TestPart testPart;

    public GroupItem() {
    }

    public GroupItem(TestType testType, TestPart testPart) {
        this.testType = testType;
        this.testPart = testPart;
    }

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    public TestPart getTestPart() {
        return testPart;
    }

    public void setTestPart(TestPart testPart) {
        this.testPart = testPart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupItem groupItem = (GroupItem) o;
        return testType == groupItem.testType &&
                testPart == groupItem.testPart;
    }

    @Override
    public int hashCode() {
        return Objects.hash(testType, testPart);
    }

    @Override
    public String toString() {
        return String.format("TestItem { %s %s }", testType, testPart);
    }

    @Override
    public int compareTo(GroupItem o) {
        int t = Integer.compare(testType.value(), o.testType.value());
        if (t != 0) {
            return Integer.compare(testPart.value(), o.testPart.value());
        }
        return t;
    }
}
