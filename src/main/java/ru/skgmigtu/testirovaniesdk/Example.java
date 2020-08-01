package ru.skgmigtu.testirovaniesdk;

import jdk.nashorn.internal.ir.LiteralNode;
import ru.skgmigtu.testirovaniesdk.models.Part;
import ru.skgmigtu.testirovaniesdk.models.QuestionAnswers;
import ru.skgmigtu.testirovaniesdk.models.SubjectValue;
import ru.skgmigtu.testirovaniesdk.models.Type;

import java.util.List;

public class Example {

    public static void main(String[] args) throws Exception {

        Testirovanie testirovanie = new Testirovanie();

        int studID = 16001;                         // ID студента (номер зачетки), от имени которого совершается действие
        String subjectName = "Intranet-технология"; // название дисциплины
        Type type = Type.RATING_1;                  // тип сдачи (Рейтинг 1, Рейтинг 2, Зачет, Экзамен)
        Part part = Part.A;                         // часть (А, В)
        int repetitions = 1;                       // количество повторений

        // получение списока вопросов и ответов
        List<QuestionAnswers> qaList = testirovanie.getQuestionsAndAnswers(studID, subjectName, type, part, repetitions);
        for (QuestionAnswers qa : qaList) {
            System.out.println(qa);
        }

        // получение списка доступных предметов
        List<SubjectValue> svList = testirovanie.availableSubjects(studID, type, part);
        for (SubjectValue subjectValue : svList) {
            System.out.println(subjectValue);
        }

    }

}
