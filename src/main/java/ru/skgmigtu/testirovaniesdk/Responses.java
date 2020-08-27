package ru.skgmigtu.testirovaniesdk;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.skgmigtu.testirovaniesdk.models.enums.Part;
import ru.skgmigtu.testirovaniesdk.models.enums.Type;

import java.io.IOException;

public class Responses {

    private String TRY_LINK;
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, " +
            "like Gecko) Chrome/81.0.4044.138 YaBrowser/20.6.0.905 Yowser/2.5 Yptp/1.23 Safari/537.36";
    private static final int CONNECTION_TIMEOUT = 60_000;

    public Responses(String baseUrl) {
        TRY_LINK = baseUrl + "/reiting_try/control.aspx";
    }

    public Connection.Response getLoginResponse() throws IOException {
        return Jsoup.connect(TRY_LINK)
                .timeout(CONNECTION_TIMEOUT)
                .method(Connection.Method.GET)
                .userAgent(USER_AGENT)
                .execute();
    }

    public Connection.Response getChooseResponse(int studID) throws IOException {
        Connection.Response loginResponse = getLoginResponse();
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

    public Connection.Response getSubjectResponse(int studID, Type type, Part part) throws IOException {
        Connection.Response chooseResponse = getChooseResponse(studID);
        Document chooseDoc = chooseResponse.parse();
        return Jsoup.connect(TRY_LINK)
                .timeout(CONNECTION_TIMEOUT)
                .cookies(chooseResponse.cookies())
                .data("__EVENTTARGET", getElementValueById(chooseDoc, "__EVENTTARGET"))
                .data("__EVENTARGUMENT", getElementValueById(chooseDoc, "__EVENTARGUMENT"))
                .data("__LASTFOCUS", getElementValueById(chooseDoc, "__LASTFOCUS"))
                .data("__VIEWSTATE", getElementValueById(chooseDoc, "__VIEWSTATE"))
                .data("modRBL", type.value())
                .data("levelRBL", part.value())
                .data("disDDL", "0")
                .method(Connection.Method.POST)
                .userAgent(USER_AGENT)
                .execute();
    }

    public Connection.Response getTestResponse(int studID, String subject, Type type, Part part) throws IOException {
        // получаем ответа со страницы выбора дисциплины в зависимости от
        // указанных типа и сложности тестирования
        Connection.Response subjectResponse = getSubjectResponse(studID, type, part);

        // получаем страницу выбора дисциплины в зависимости от
        // выбранных ранее типа и сложности тестирования
        Document subjectPage = subjectResponse.parse();

        // получаем value выбранной дисциплины
        String subjectValue = getSubjectValue(subjectPage, subject);
        if (subjectValue == null) {
            System.err.println(String.format("Дисциплина '%s' не найдена для студента с ID=%d", subject, studID));
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

}
