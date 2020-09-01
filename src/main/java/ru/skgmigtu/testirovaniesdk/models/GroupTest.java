package ru.skgmigtu.testirovaniesdk.models;

import java.io.Serializable;
import java.util.List;

import static ru.skgmigtu.testirovaniesdk.Testirovanie.REPETITION_COUNT;

public class GroupTest implements Serializable, Comparable<String> {

    private int studID;
    private String subjectName;
    private List<GroupItem> groupItems;
    private int repetitions;

    public GroupTest() {

    }

    public GroupTest(int studID, String subjectName, List<GroupItem> groupItems, int repetitions) {
        this.studID = studID;
        this.subjectName = subjectName;
        this.groupItems = groupItems;
        checkRepetitions(repetitions);
        this.repetitions = repetitions;
    }

    private void checkRepetitions(int repetitions) {
        if (repetitions < 1)
            throw new IllegalArgumentException("значение переменной repetitions не может быть меньше 1");
        if (REPETITION_COUNT < repetitions)
            throw new IllegalArgumentException("значение переменной repetitions слишком большое (макс. " + REPETITION_COUNT + ")");
    }

    public int getStudID() {
        return studID;
    }

    public void setStudID(int studID) {
        this.studID = studID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<GroupItem> getGroupItems() {
        return groupItems;
    }

    public void setGroupItems(List<GroupItem> groupItems) {
        this.groupItems = groupItems;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    @Override
    public int compareTo(String o) {
        return subjectName.compareTo(o);
    }

}
