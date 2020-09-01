package ru.skgmigtu.testirovaniesdk;

import ru.skgmigtu.testirovaniesdk.Testirovanie.BaseUrl;
import ru.skgmigtu.testirovaniesdk.Testirovanie.Part;
import ru.skgmigtu.testirovaniesdk.Testirovanie.Type;
import ru.skgmigtu.testirovaniesdk.models.GroupItem;
import ru.skgmigtu.testirovaniesdk.models.GroupTest;
import ru.skgmigtu.testirovaniesdk.models.QuestionAnswers;

import java.util.ArrayList;
import java.util.List;

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
                }},
                5
        );
        gtList.add(gt1);

        System.out.println("Списока вопросов и ответов:");
        List<QuestionAnswers> qaList = testirovanie.getQuestionsAndAnswers(gtList);
        for (QuestionAnswers qa : qaList) {
            System.out.println(qa);
        }
        System.out.println(qaList.size());

    }

}
