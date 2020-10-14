package ru.skgmigtu.testirovaniesdk;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.skgmigtu.testirovaniesdk.models.Module;
import ru.skgmigtu.testirovaniesdk.models.Part;

import java.io.IOException;

public class Responses {

    private String TRY_LINK;
    private String QUESTION_LIST;
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, " +
            "like Gecko) Chrome/81.0.4044.138 YaBrowser/20.6.0.905 Yowser/2.5 Yptp/1.23 Safari/537.36";
    private static final int CONNECTION_TIMEOUT = 60_000;

    public Responses(String baseUrl) {
        TRY_LINK = baseUrl + "/reiting_try/control.aspx";
        QUESTION_LIST = baseUrl + "/FreeView/getTasks.aspx";
    }

    private Connection.Response getChooseResponse(int studID) throws IOException {
        Connection.Response loginResponse = getPage(TRY_LINK);
        Document loginDoc = loginResponse.parse();
        return Jsoup.connect(TRY_LINK)
                .timeout(CONNECTION_TIMEOUT)
                .cookies(loginResponse.cookies())
                .data("__VIEWSTATE", getElementValueById(loginDoc, "__VIEWSTATE"))
                .data("LoginTB", studID + "")
                .data("LogIn.x", "28")
                .data("LogIn.y", "16")
                .method(Connection.Method.POST)
                .userAgent(USER_AGENT)
                .execute();
    }

    Connection.Response getSubjectResponse(int studID, Module module, Part part) throws IOException {
        Connection.Response chooseResponse = getChooseResponse(studID);
        Document chooseDoc = chooseResponse.parse();
        return Jsoup.connect(TRY_LINK)
                .timeout(CONNECTION_TIMEOUT)
                .cookies(chooseResponse.cookies())
                .data("__EVENTTARGET", getElementValueById(chooseDoc, "__EVENTTARGET"))
                .data("__EVENTARGUMENT", getElementValueById(chooseDoc, "__EVENTARGUMENT"))
                .data("__LASTFOCUS", getElementValueById(chooseDoc, "__LASTFOCUS"))
                .data("__VIEWSTATE", getElementValueById(chooseDoc, "__VIEWSTATE"))
                .data("modRBL", module.value() + "")
                .data("levelRBL", part.value() + "")
                .data("disDDL", "0")
                .method(Connection.Method.POST)
                .userAgent(USER_AGENT)
                .execute();
    }

    public Connection.Response getTestResponse(int studID, String subject, Module module, Part part) throws IOException {
        // получаем ответа со страницы выбора дисциплины в зависимости от
        // указанных типа и сложности тестирования
        Connection.Response subjectResponse = getSubjectResponse(studID, module, part);

        // получаем страницу выбора дисциплины в зависимости от
        // выбранных ранее типа и сложности тестирования
        Document subjectPage = subjectResponse.parse();

        // получаем value выбранной дисциплины
        String subjectValue = getSubjectValue(subjectPage, subject);
        if (subjectValue == null) {
            System.err.println(String.format("Дисциплина '%s (%s, %s)' не найдена для студента с ID=%d", subject, module, part, studID));
            return null;
        }

        // получаем соединение со страницей тестирования
        return Jsoup.connect(TRY_LINK)
                .timeout(CONNECTION_TIMEOUT)
                .cookies(subjectResponse.cookies())
                .data("__EVENTTARGET", getElementValueById(subjectPage, "__EVENTTARGET"))
                .data("__EVENTARGUMENT", getElementValueById(subjectPage, "__EVENTARGUMENT"))
                .data("__LASTFOCUS", getElementValueById(subjectPage, "__LASTFOCUS"))
                .data("__VIEWSTATE", getElementValueById(subjectPage, "__VIEWSTATE"))
                .data("modRBL", module.value() + "")
                .data("levelRBL", part.value() + "")
                .data("disDDL", subjectValue)
                .method(Connection.Method.POST)
                .userAgent(USER_AGENT)
                .execute();
    }

    public Connection.Response getQuestionListResponse(String faculty, String group, int semester, Module module, String discipline) throws IOException {
        Connection.Response clone = null, response = getPage(QUESTION_LIST);
        Document doc = response.parse();

        String[] keys = new String[]{
                "yearDDL", "FakultetDDL", "groupDDL", "semDDL", "modDDList", "DisList"
        };

        for (int i = 0; i < keys.length; i++) {
            switch (i) {
                case 0:
                    response = questionListResponseBuilder(response, doc, keys[i], "2020");
                    break;
                case 1:
                    response = questionListResponseBuilder(response, doc, keys[i], getFacultyValue(doc, faculty));
                    break;
                case 2:
                    response = questionListResponseBuilder(response, doc, keys[i], getGroupValue(doc, group));
                    break;
                case 3:
                    response = questionListResponseBuilder(response, doc, keys[i], semester + "");
                    break;
                case 4:
                    response = questionListResponseBuilder(response, doc, keys[i], module.value() + "");
                    break;
                case 5:
                    response = questionListResponseBuilder(response, doc, keys[i], getDisciplineValue(doc, discipline));
                    break;
            }

            clone = response.bufferUp();
            doc = response.parse();
        }

        return clone;
    }

    private Connection.Response getPage(String link) throws IOException {
        return Jsoup.connect(link)
                .timeout(CONNECTION_TIMEOUT)
                .method(Connection.Method.GET)
                .userAgent(USER_AGENT)
                .execute();
    }

    private Connection.Response questionListResponseBuilder(Connection.Response response, Document doc, String... keyval) throws IOException {
        return Jsoup.connect(QUESTION_LIST)
                .timeout(CONNECTION_TIMEOUT)
                .cookies(response.cookies())
                .data("__EVENTTARGET", getElementValueById(doc, "__EVENTTARGET"))
                .data("__EVENTARGUMENT", getElementValueById(doc, "__EVENTARGUMENT"))
                .data("__LASTFOCUS", getElementValueById(doc, "__LASTFOCUS"))
                .data("__VIEWSTATE", getElementValueById(doc, "__VIEWSTATE"))
                .data("__EVENTVALIDATION", getElementValueById(doc, "__EVENTVALIDATION"))
                .data(keyval)
                .method(Connection.Method.POST)
                .userAgent(USER_AGENT)
                .execute();
    }

    private String getSubjectValue(Document dos, String subjectName) {
        // получаем value выборанной дисциплины (на странице тестирования)
        String subjectValue = null;
        Elements children = dos.select("#disDDL > option");
        for (Element child : children) {
            if (subjectName.equals(child.text())) {
                subjectValue = child.attr("value");
            }
        }
        return subjectValue;
    }

    private String getFacultyValue(Document dos, String facultyName) {
        // получаем value выборанного факультета
        String facultyValue = null;
        Elements children = dos.select("select[name~=FakultetDDL] > option");
        for (Element child : children) {
            if (facultyName.equals(child.text())) {
                facultyValue = child.attr("value");
            }
        }
        return facultyValue;
    }

    private String getGroupValue(Document dos, String groupName) {
        // получаем value выборанной группы
        String groupValue = null;
        Elements children = dos.select("select[name~=groupDDL] > option");
        for (Element child : children) {
            if (groupName.equals(child.text())) {
                groupValue = child.attr("value");
            }
        }
        return groupValue;
    }

    private String getDisciplineValue(Document dos, String disciplineName) {
        // получаем value выборанной дисциплины (на странице получения списка вопросов)
        String disciplineValue = null;
        Elements children = dos.select("select[name~=DisList] > option");
        for (Element child : children) {
            if (disciplineName.equals(child.text())) {
                disciplineValue = child.attr("value");
            }
        }
        return disciplineValue;
    }

    private String getElementValueById(Document doc, String id) {
        return doc.getElementById(id).attr("value");
    }

}
