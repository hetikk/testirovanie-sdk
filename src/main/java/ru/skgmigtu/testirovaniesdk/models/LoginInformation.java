package ru.skgmigtu.testirovaniesdk.models;

import ru.skgmigtu.testirovaniesdk.Testirovanie.Part;
import ru.skgmigtu.testirovaniesdk.Testirovanie.Type;

import java.io.Serializable;
import java.util.Objects;

import static ru.skgmigtu.testirovaniesdk.Testirovanie.REPETITION_COUNT;

public class LoginInformation implements Serializable {

    private int studID;
    private String subjectName;
    private Type type;
    private Part part;
    private int repetitions;

    public LoginInformation() {
    }

    public LoginInformation(int studID, String subjectName, Type type, Part part) {
        this.studID = studID;
        this.subjectName = subjectName;
        this.type = type;
        this.part = part;
        repetitions = 1;
    }

    public LoginInformation(int studID, String subjectName, Type type, Part part, int repetitions) {
        this.studID = studID;
        this.subjectName = subjectName;
        this.type = type;
        this.part = part;
        checkRepetitions(repetitions);
        this.repetitions = repetitions;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        checkRepetitions(repetitions);
        this.repetitions = repetitions;
    }

    private void checkRepetitions(int repetitions) {
        if (repetitions < 1)
            throw new IllegalArgumentException("значение переменной repetitions не может быть меньше 1");
        if (REPETITION_COUNT < repetitions)
            throw new IllegalArgumentException("значение переменной repetitions слишком большое (макс. " + REPETITION_COUNT + ")");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginInformation that = (LoginInformation) o;
        return studID == that.studID &&
                repetitions == that.repetitions &&
                subjectName.equals(that.subjectName) &&
                type == that.type &&
                part == that.part;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studID, subjectName, type, part, repetitions);
    }

}
