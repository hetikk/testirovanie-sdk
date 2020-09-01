package ru.skgmigtu.testirovaniesdk;

import ru.skgmigtu.testirovaniesdk.Testirovanie.BaseUrl;
import ru.skgmigtu.testirovaniesdk.Testirovanie.Part;
import ru.skgmigtu.testirovaniesdk.Testirovanie.Type;
import ru.skgmigtu.testirovaniesdk.models.*;

import java.util.ArrayList;
import java.util.List;

public class Example {

    public static void main(String[] args) throws Exception {

        Testirovanie testirovanie = new Testirovanie(BaseUrl.REMOTE);

        int studID = 16001;                         // ID студента (номер зачетки), от имени которого совершается действие
        String subjectName = "Intranet-технология"; // название дисциплины
        Type type = Type.RATING_1;                  // тип сдачи (Рейтинг 1, Рейтинг 2, Зачет, Экзамен)
        Part part = Part.A;                         // часть (А, В)
        int repetitions = 1;                        // количество повторений

        GroupTest gt = new GroupTest(
                16001,
                "Intranet-технология",
                new ArrayList<GroupItem>() {{
                    add(new GroupItem(Type.RATING_1, Part.A));
                    add(new GroupItem(Type.RATING_1, Part.B));
                    add(new GroupItem(Type.RATING_2, Part.A));
                    add(new GroupItem(Type.RATING_2, Part.B));
                    add(new GroupItem(Type.ZACHET, Part.A));
                }},
                1
        );

        System.out.println("Списока вопросов и ответов:");
        List<QuestionAnswers> qaList = testirovanie.getQuestionsAndAnswers(gt);
        for (QuestionAnswers qa : qaList) {
            System.out.println(qa);
        }

        System.out.println("\nСписок доступных предметов:");
        List<SubjectValue> svList = testirovanie.availableSubjects(studID, type, part);
        for (SubjectValue subjectValue : svList) {
            System.out.println(subjectValue);
        }

    }

}
