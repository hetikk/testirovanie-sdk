package ru.skgmigtu.testirovaniesdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.skgmigtu.testirovaniesdk.models.Item;
import ru.skgmigtu.testirovaniesdk.models.QuestionAnswers;
import ru.skgmigtu.testirovaniesdk.models.SubjectValue;
import ru.skgmigtu.testirovaniesdk.models.enums.BaseUrl;
import ru.skgmigtu.testirovaniesdk.models.enums.Part;
import ru.skgmigtu.testirovaniesdk.models.enums.Type;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Testirovanie {

    private final Responses responses;

    private static final int REPETITION_COUNT = 35;

    public Testirovanie(BaseUrl baseUrl) {
        responses = new Responses(baseUrl.value());
    }

    public Testirovanie(String baseUrl) {
        responses = new Responses(baseUrl);
    }

    public List<QuestionAnswers> getQuestionsAndAnswers(int studID, String subject, Type type, Part part) throws Exception {
        // получаем соединение со страницей тестирования
        Connection.Response testConnection = responses.getTestResponse(studID, subject, type, part);
        return testConnection == null ?
                Collections.emptyList() :
                parse(testConnection.parse()); // получаем страницу тестирования и парсим ее
    }

    public List<QuestionAnswers> getQuestionsAndAnswers(int studID, String subject, Type type, Part part, int repetitions) throws Exception {
        if (repetitions < 1)
            throw new IllegalArgumentException("значение переменной repetitions не может быть меньше 1");
        if (REPETITION_COUNT < repetitions)
            throw new IllegalArgumentException("значение переменной repetitions слишком большое (макс. " + REPETITION_COUNT + ")");

        final int threadCount = Math.max(repetitions / 2, 1);

        Set<QuestionAnswers> result = Collections.synchronizedSet(new TreeSet<>());
        ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
        for (int i = studID; i <= studID + repetitions; i++) {
            int finalI = i;
            threadPool.execute(() -> {
                try {
                    List<QuestionAnswers> list = getQuestionsAndAnswers(finalI, subject, type, part);
                    result.addAll(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        threadPool.shutdown();

        boolean done = threadPool.awaitTermination(repetitions * 45, TimeUnit.SECONDS);
        if (!done) {
            threadPool.shutdownNow();
        }
        return new ArrayList<>(result);
    }

    public List<QuestionAnswers> parse(Document doc) {
        List<QuestionAnswers> result = new ArrayList<>();

        Elements rows = doc.select("#mainPanel > table > tbody > tr:nth-child(odd)");
        for (Element row : rows) {
            int questionID = Integer.parseInt(row.select("*[id~=^taskRep_ctl[0-9]{2}_LabelId]").first().text());
            String questionText = row.select("*[id~=^taskRep_ctl[0-9]{2}_task1Label$]").first().text();

            Elements answersBlock = row.select("*[id~=(^taskRep_ctl[0-9]{2}_RadioButtonList1$)|(^taskRep_ctl[0-9]{2}_CheckBoxList1$)] td");
            List<Item> answers = new ArrayList<>();
            boolean multipleChoice = false;
            for (Element item : answersBlock) {
                Element input = item.child(0);
                if (input.attr("type").equals("checkbox"))
                    multipleChoice = true;
                int inputID = Integer.parseInt(input.attr("value"));
                String text = item.child(1).text();
                answers.add(new Item(inputID, text));
            }

            Item rightAnswer = multipleChoice ?
                    null :
                    answers.stream().min(Comparator.comparingInt(Item::getId)).get();

            result.add(new QuestionAnswers(
                    new Item(questionID, questionText),
                    answers,
                    rightAnswer,
                    multipleChoice
            ));
        }

        Collections.sort(result);

        return result;
    }

    public List<QuestionAnswers> parse(File file) throws Exception {
        Document document = Jsoup.parse(file, "UTF-8");
        return parse(document);
    }

    public List<SubjectValue> availableSubjects(int studID, Type type, Part part) throws IOException {
        Connection.Response subjectResponse = responses.getSubjectResponse(studID, type, part);
        Document subjectDocument = subjectResponse.parse();
        Elements children = subjectDocument.select("#disDDL > option");

        List<SubjectValue> result = new ArrayList<>(children.size());
        for (Element child : children) {
            int value = Integer.parseInt(child.attr("value"));
            if (value != 0) {
                result.add(new SubjectValue(
                        child.text(),
                        value
                ));
            }
        }

        return result;
    }

}
