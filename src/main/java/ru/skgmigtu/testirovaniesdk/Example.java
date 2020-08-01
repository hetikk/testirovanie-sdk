package ru.skgmigtu.testirovaniesdk;

import ru.skgmigtu.testirovaniesdk.models.Part;
import ru.skgmigtu.testirovaniesdk.models.QuestionAnswers;
import ru.skgmigtu.testirovaniesdk.models.Type;

import java.util.List;

public class Example {

    public static void main(String[] args) throws Exception {

        final Testirovanie testirovanie = new Testirovanie();

        List<QuestionAnswers> answers = testirovanie.getQuestionsAndAnswers(
                16001, "Intranet-технология", Type.RATING_1, Part.A);

        for (QuestionAnswers answer : answers) {
            System.out.println(answer);
            System.out.println();
        }

    }

}
