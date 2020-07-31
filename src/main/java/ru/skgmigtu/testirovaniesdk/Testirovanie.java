package ru.skgmigtu.testirovaniesdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.skgmigtu.testirovaniesdk.models.Item;
import ru.skgmigtu.testirovaniesdk.models.Part;
import ru.skgmigtu.testirovaniesdk.models.QuestionAnswers;
import ru.skgmigtu.testirovaniesdk.models.Type;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Testirovanie {

    private static final String TRY_LINK = "http://testirovanie.skgmi-gtu.ru/reiting_try/control.aspx";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, " +
            "like Gecko) Chrome/81.0.4044.138 YaBrowser/20.6.0.905 Yowser/2.5 Yptp/1.23 Safari/537.36";
    private static final int CONNECTION_TIMEOUT = 60_000;

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public List<QuestionAnswers> getQuestionsAndAnswers(int studID, String subject, Type type, Part part) throws Exception {
        // получаем соединение со страницей тестирования
        Connection.Response testConnection = getTestResponse(studID, subject, type, part);
        return testConnection == null ?
                Collections.emptyList() :
                parse(testConnection.parse()); // получаем страницу тестирования и парсим ее
    }

    public List<QuestionAnswers> getQuestionsAndAnswers(int studID, String subject, Type type, Part part, int repetitions) throws Exception {
        final int threadCount = repetitions / 2;

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

    private Connection.Response getTestResponse(int studID, String subject, Type type, Part part) throws IOException {
        // создаем соединение со страницей авторизации для пробного тестирования
        Connection.Response loginConnection = Jsoup.connect(TRY_LINK)
                .timeout(CONNECTION_TIMEOUT)
                .method(Connection.Method.GET)
                .userAgent(USER_AGENT)
                .execute();

        // получаем страницу авторизации пробного тестирования
        Document loginPage = loginConnection.parse();

        // получаем соединение со страницей выбора типа и сложности тестирования
        Connection.Response chooseConnection = Jsoup.connect(TRY_LINK)
                .timeout(CONNECTION_TIMEOUT)
                .cookies(loginConnection.cookies())
                .data("__VIEWSTATE", getElementValueById(loginPage, "__VIEWSTATE"))
                .data("LoginTB", studID + "")
                .data("LogIn.x", "28")
                .data("LogIn.y", "16")
                .method(Connection.Method.POST)
                .userAgent(USER_AGENT)
                .execute();

        // получаем страницу выбора типа и сложности тестирования
        Document choosePage = chooseConnection.parse();

        // получаем соединение со страницей выбора дисциплины в зависимости от
        // выбранных ранее типа и сложности тестирования
        Connection.Response subjectConnection = Jsoup.connect(TRY_LINK)
                .timeout(CONNECTION_TIMEOUT)
                .cookies(chooseConnection.cookies())
                .data("__EVENTTARGET", getElementValueById(choosePage, "__EVENTTARGET"))
                .data("__EVENTARGUMENT", getElementValueById(choosePage, "__EVENTARGUMENT"))
                .data("__LASTFOCUS", getElementValueById(choosePage, "__LASTFOCUS"))
                .data("__VIEWSTATE", getElementValueById(choosePage, "__VIEWSTATE"))
                .data("modRBL", type.value())
                .data("levelRBL", part.value())
                .data("disDDL", "0")
                .method(Connection.Method.POST)
                .userAgent(USER_AGENT)
                .execute();

        // получаем страницу выбора дисциплины в зависимости от
        // выбранных ранее типа и сложности тестирования
        Document subjectPage = subjectConnection.parse();

        // получаем value выбранной дисциплины
        String subjectValue = getSubjectValue(subjectPage, subject);

        if (subjectValue == null) {
            System.err.println(String.format("Дисциплина '%s' не найдена для студента с ID=%d", subject, studID));
            return null;
        }

        // получаем соединение со страницей тестирования
        return Jsoup.connect(TRY_LINK)
                .timeout(CONNECTION_TIMEOUT)
                .cookies(subjectConnection.cookies())
                .data("__EVENTTARGET", getElementValueById(choosePage, "__EVENTTARGET"))
                .data("__EVENTARGUMENT", getElementValueById(choosePage, "__EVENTARGUMENT"))
                .data("__LASTFOCUS", getElementValueById(choosePage, "__LASTFOCUS"))
                .data("__VIEWSTATE", getElementValueById(choosePage, "__VIEWSTATE"))
                .data("modRBL", type.value())
                .data("levelRBL", part.value())
                .data("disDDL", subjectValue)
                .method(Connection.Method.POST)
                .userAgent(USER_AGENT)
                .execute();
    }

    private String getSubjectValue(Document dos, String subjectName) {
        // получаем value выборанной дисциплины
        String subjectValue = null;
        Elements children = dos.select("#disDDL > option");
        for (Element child : children) {
            if (subjectName.equals(child.text())) {
                subjectValue = child.attr("value");
            }
        }
        return subjectValue;
    }

    private String getElementValueById(Document doc, String id) {
        return doc.getElementById(id).attr("value");
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

}
