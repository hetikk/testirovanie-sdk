package ru.skgmigtu.testirovaniesdk;

import ru.skgmigtu.testirovaniesdk.Testirovanie.BaseUrl;
import ru.skgmigtu.testirovaniesdk.Testirovanie.Part;
import ru.skgmigtu.testirovaniesdk.Testirovanie.Type;
import ru.skgmigtu.testirovaniesdk.models.LoginInformation;
import ru.skgmigtu.testirovaniesdk.models.QuestionAnswers;
import ru.skgmigtu.testirovaniesdk.models.SubjectValue;

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

        LoginInformation li = new LoginInformation( // или в виде одного объекта
                studID,
                subjectName,
                type,
                part,
                repetitions);

        // можно даже так
        List<LoginInformation> liList = new ArrayList<LoginInformation>() {{
            add(new LoginInformation(16001, subjectName, Type.RATING_1, Part.A, 3));
            add(new LoginInformation(16001, subjectName, Type.RATING_1, Part.B, 3));
            add(new LoginInformation(16001, subjectName, Type.RATING_2, Part.A, 3));
            add(new LoginInformation(16001, subjectName, Type.RATING_2, Part.B, 3));
        }};

        System.out.println("Списока вопросов и ответов:");
        List<QuestionAnswers> qaList = testirovanie.getQuestionsAndAnswers(liList);
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
