package ru.skgmigtu.testirovaniesdk;

import ru.skgmigtu.testirovaniesdk.models.*;

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
                    add(new GroupItem(TestType.RATING_1, TestPart.A));
//                    add(new GroupItem(Type.RATING_1, Part.B));
//                    add(new GroupItem(Type.RATING_2, Part.A));
//                    add(new GroupItem(Type.RATING_2, Part.B));
//                    add(new GroupItem(Type.EXAM, Part.A));
//                    add(new GroupItem(Type.EXAM, Part.B));
                }},
                1
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

    }

}
