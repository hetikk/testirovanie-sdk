package ru.skgmigtu.testirovaniesdk;

import ru.skgmigtu.testirovaniesdk.Testirovanie.BaseUrl;
import ru.skgmigtu.testirovaniesdk.Testirovanie.Part;
import ru.skgmigtu.testirovaniesdk.Testirovanie.Type;
import ru.skgmigtu.testirovaniesdk.models.GroupItem;
import ru.skgmigtu.testirovaniesdk.models.GroupTest;
import ru.skgmigtu.testirovaniesdk.models.QuestionAnswers;
import ru.skgmigtu.testirovaniesdk.models.SubjectValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Example {

    public static void main(String[] args) throws Exception {

        Testirovanie testirovanie = new Testirovanie(BaseUrl.REMOTE);

        List<GroupTest> gtList = new ArrayList<>();

        GroupTest gt1 = new GroupTest(
                16001,
                "Intranet-технология",
                new ArrayList<GroupItem>() {{
                    add(new GroupItem(Type.RATING_1, Part.A));
                    add(new GroupItem(Type.RATING_1, Part.B));
                    add(new GroupItem(Type.RATING_2, Part.A));
                    add(new GroupItem(Type.RATING_2, Part.B));
                    add(new GroupItem(Type.EXAM, Part.A));
                    add(new GroupItem(Type.EXAM, Part.B));
                }},
                35
        );
        gtList.add(gt1);

        System.out.println("Списока вопросов и ответов:");
        Map<String, List<QuestionAnswers>> qaMap = testirovanie.getQuestionsAndAnswers(gtList);
        for (Map.Entry<String, List<QuestionAnswers>> entry : qaMap.entrySet()) {
            System.out.println("Предмет: " + entry.getKey());
            for (QuestionAnswers qa : entry.getValue()) {
                System.out.println(qa);
            }
            System.out.println();
        }

        System.out.println("Списока вопросов и ответов:");
        List<SubjectValue> asList = testirovanie.availableSubjects(16001, Type.RATING_1, Part.A);
        for (SubjectValue as : asList) {
            System.out.println(as);
        }

    }

}
