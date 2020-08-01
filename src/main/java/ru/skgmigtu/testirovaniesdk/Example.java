package ru.skgmigtu.testirovaniesdk;

import ru.skgmigtu.testirovaniesdk.models.Part;
import ru.skgmigtu.testirovaniesdk.models.SubjectValue;
import ru.skgmigtu.testirovaniesdk.models.Type;

import java.util.List;

public class Example {

    public static void main(String[] args) throws Exception {

        final Testirovanie testirovanie = new Testirovanie();

        List<SubjectValue> answers = testirovanie.getAvailableSubjects(
                16001, Type.RATING_1, Part.A);

        for (SubjectValue answer : answers) {
            System.out.println(answer);
        }

    }

}
