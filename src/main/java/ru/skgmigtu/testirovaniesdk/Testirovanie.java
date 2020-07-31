package ru.skgmigtu.testirovaniesdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.skgmigtu.testirovaniesdk.models.Part;
import ru.skgmigtu.testirovaniesdk.models.Type;

import java.io.IOException;

public class Testirovanie {

    private static final String TRY_LINK = "http://testirovanie.skgmi-gtu.ru/reiting_try/control.aspx";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, " +
            "like Gecko) Chrome/81.0.4044.138 YaBrowser/20.6.0.905 Yowser/2.5 Yptp/1.23 Safari/537.36";
    private static final int CONNECTION_TIMEOUT = 60_000;

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

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

}
