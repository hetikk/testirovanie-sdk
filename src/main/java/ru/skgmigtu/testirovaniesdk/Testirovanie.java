package ru.skgmigtu.testirovaniesdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.skgmigtu.testirovaniesdk.models.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Testirovanie {

    private final Responses responses;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final int REPETITION_COUNT = 40;

    public Testirovanie(BaseUrl baseUrl) {
        responses = new Responses(baseUrl.value());
    }

    public Testirovanie(String baseUrl) {
        responses = new Responses(baseUrl);
    }

    public List<QuestionAnswers> getQuestionsAndAnswers(int studID, String subject, Module module, Part part) throws Exception {
        // получаем соединение со страницей тестирования
        System.out.println(String.format("{ %5d, %60s, %8s, %s, %s }", studID, subject, module, part, Thread.currentThread().getName()));
        Connection.Response testConnection = responses.getTestResponse(studID, subject, module, part);
        return testConnection == null ?
                Collections.emptyList() :
                parse(testConnection.parse()); // получаем страницу тестирования и парсим ее
    }

    public List<QuestionAnswers> getQuestionsAndAnswers(int studID, String subject, Module module, Part part, int repetitions) throws Exception {
        if (repetitions < 1)
            throw new IllegalArgumentException("значение переменной repetitions не может быть меньше 1");
        if (REPETITION_COUNT < repetitions)
            throw new IllegalArgumentException("значение переменной repetitions слишком большое (макс. " + REPETITION_COUNT + ")");

        final int threadCount = Math.min(
                Math.max(repetitions / 2, 1),
                Runtime.getRuntime().availableProcessors()
        );

        Set<QuestionAnswers> result = Collections.synchronizedSet(new TreeSet<>());
        ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
        for (int i = studID; i < studID + repetitions; i++) {
            int finalI = i;
            threadPool.execute(() -> {
                try {
                    result.addAll(getQuestionsAndAnswers(finalI, subject, module, part));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        threadPool.shutdown();

        try {
            threadPool.awaitTermination(repetitions * 45, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(result);
    }

    public List<QuestionAnswers> getQuestionsAndAnswers(GroupTest gt) throws Exception {
        Set<QuestionAnswers> result = new HashSet<>();

        List<GroupItem> groupItems = gt.getGroupItems();
        for (GroupItem item : groupItems) {
            result.addAll(getQuestionsAndAnswers(
                    gt.getStudID(),
                    gt.getSubjectName(),
                    item.getModule(),
                    item.getPart(),
                    gt.getRepetitions()
            ));
        }

        return new ArrayList<>(result);
    }

    public Map<String, List<QuestionAnswers>> getQuestionsAndAnswers(List<GroupTest> gtList) throws Exception {
        // TODO: сделать обработку в нескольких потоках

        Map<String, List<QuestionAnswers>> result = new TreeMap<>();

        for (GroupTest gt : gtList) {
            result.put(gt.getSubjectName(), getQuestionsAndAnswers(gt));
        }

        return result;
    }

    public List<String> getQuestionList(String faculty, String group, int semester, Module module, String discipline) throws Exception {
        Connection.Response response = responses
                .getQuestionListResponse(faculty, group, semester, module, discipline);
        Document doc = response.parse();

        Elements questions = doc.selectFirst("#oldTasksGV > tbody").children();

        if (questions.size() > 0) questions.remove(0);

        return questions.stream()
                .map(Element::text)
//                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    public List<QuestionAnswers> parse(Document doc) {
        List<QuestionAnswers> result = new ArrayList<>();

        Elements rows = doc.select("#mainPanel > table > tbody > tr:nth-child(odd)");
        for (Element row : rows) {
            int questionID = Integer.parseInt(row.select("*[id~=^taskRep_ctl[0-9]{2}_LabelId]").first().text());
            String questionText = row.select("*[id~=^taskRep_ctl[0-9]{2}_task1Label$]").first().text();

            Elements answersBlock = row.select("*[id~=(^taskRep_ctl[0-9]{2}_RadioButtonList1$)|(^taskRep_ctl[0-9]{2}_CheckBoxList1$)] td");
            List<QAItem> answers = new ArrayList<>();
            boolean multipleChoice = false;
            for (Element item : answersBlock) {
                Element input = item.child(0);
                if (input.attr("type").equals("checkbox"))
                    multipleChoice = true;
                int inputID = Integer.parseInt(input.attr("value"));
                String text = item.child(1).text();
                answers.add(new QAItem(inputID, text));
            }

            QAItem rightAnswer = multipleChoice ?
                    null :
                    answers.stream().min(Comparator.comparingInt(QAItem::getId)).get();

            result.add(new QuestionAnswers(
                    new QAItem(questionID, questionText),
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

    public List<SubjectValue> availableSubjects(int studID, Module module, Part part) throws IOException {
        Connection.Response subjectResponse = responses.getSubjectResponse(studID, module, part);
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

    public void save(String filename, Object obj) throws IOException {
        if (!filename.endsWith(".json"))
            filename = filename + ".json";
        String json = GSON.toJson(obj);
        FileUtils.writeStringToFile(new File(filename), json, StandardCharsets.UTF_8);
    }

}
