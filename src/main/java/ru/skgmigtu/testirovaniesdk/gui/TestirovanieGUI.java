package ru.skgmigtu.testirovaniesdk.gui;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebTextArea;
import com.alee.laf.text.WebTextField;
import com.alee.laf.window.WebFrame;
import ru.skgmigtu.testirovaniesdk.Testirovanie;
import ru.skgmigtu.testirovaniesdk.models.GroupItem;
import ru.skgmigtu.testirovaniesdk.models.GroupTest;
import ru.skgmigtu.testirovaniesdk.models.QuestionAnswers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.*;
import java.util.List;

public class TestirovanieGUI extends WebFrame {

    private Point position;

    private static Testirovanie testirovanie = new Testirovanie(Testirovanie.BaseUrl.REMOTE);

    public TestirovanieGUI() {
        setLayout(null);
        setDefaultCloseOperation(WebFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(1300, 613);
        setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point popupLocation = new Point();
        popupLocation.x = (screenSize.width - getWidth()) / 2;
        popupLocation.y = (screenSize.height - getHeight()) / 2;
        setLocation(popupLocation);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                position = e.getPoint();
                getComponentAt(position);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                int xMoved = (thisX + e.getX()) - (thisX + position.x);
                int yMoved = (thisY + e.getY()) - (thisY + position.y);

                int X = thisX + xMoved;
                int Y = thisY + yMoved;

                setLocation(X, Y);
            }
        });

        String[] columns = {
                "Название дисциплины",
                "Рейтинг 1, Часть А",
                "Рейтинг 1, Часть Б",
                "Рейтинг 2, Часть А",
                "Рейтинг 2, Часть Б",
                "Зачет, Часть А",
                "Зачет, Часть Б",
                "Экзамен, Часть А",
                "Экзамен, Часть Б",
        };

        String[] subjects = new TreeSet<String>() {{
            add("Администрирование операционных систем");
            add("Алгоритмические основы современной компьютерной графики");
            add("Моделирование систем");
            add("Операционные системы");
            add("Программирование на Ассемблера");
            add("Программирование на С#");
            add("Программирование на С++");
            add("Параллельная обработка данных");
            add("Разработка web-сценариев для серверных приложений");
            add("Разработка мобильных приложений");
            add("Системное программное обеспечение");
            add("Системы мультимедиа");
            add("Сетевые технологии");
            add("Стандартизация и сертификация программного обеспечения");
            add("Технология программирования");
            add("ЭВМ и периферийные устройства");
            add("Intranet-технология");
            add("Информационные технологии");
            add("Автоматизация систем экологической безопасности производства");
            add("Патентные исследования");
            add("Подсистемы планирования в АСУ твердосплавного производства");
            add("Теория принятия решений");
            add("Технология разработки систем искусственного интеллекта");
            add("Экспертные системы и базы знаний");
            add("Вступительный экзамен в магистратуру АСУ");
            add("Компьютерное моделирование");
            add("Пролог");
            add("Экспертные системы в поиске и анализе перспективности разработок месторождений полезных ископаемых");
            add("Базы данных");
            add("Государственный экзамен");
            add("Защита информации");
            add("Методы оптимизации");
            add("Программирование на языке высокого уровня");
            add("Проектирование оптимального программного обеспечения");
        }}.toArray(new String[0]);

        Object[][] data = new Object[subjects.length][columns.length];
        for (int i = 0; i < data.length; i++) {
            data[i][0] = subjects[i];
            data[i][1] = Boolean.FALSE;
            data[i][2] = Boolean.FALSE;
            data[i][3] = Boolean.FALSE;
            data[i][4] = Boolean.FALSE;
            data[i][5] = Boolean.FALSE;
            data[i][6] = Boolean.FALSE;
            data[i][7] = Boolean.FALSE;
            data[i][8] = Boolean.FALSE;
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            public Class<?> getColumnClass(int column) {
                return data[0][column].getClass();
            }
        };

        WebTable table = new WebTable(tableModel);
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.setFontSize(13);

        int boolWidth = 120;
        TableColumn col;
        for (int i = 1; i < 9; i++) {
            col = table.getColumnModel().getColumn(i);
            col.setMinWidth(boolWidth);
            col.setMaxWidth(boolWidth);
        }

        JScrollPane tableScroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tableScroll.setBounds(15, 15, 1265, 268);
        add(tableScroll);

        WebTextArea textArea = new WebTextArea();
        textArea.setFontName("Monospace");
        textArea.setFontSize(13);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        WebScrollPane areaScroll = new WebScrollPane(textArea);
        areaScroll.setBounds(15, 300, 1005, 268);
        add(areaScroll);

        WebLabel repetitionsNumberLbl = new WebLabel("Количество повторений");
        repetitionsNumberLbl.setBounds(1035, 300, 200, 30);
        repetitionsNumberLbl.setFontSize(13);
        add(repetitionsNumberLbl);

        WebTextField repetitionsNumberTF = new WebTextField("1");
        repetitionsNumberTF.setBounds(1220, 300, 60, 30);
        repetitionsNumberTF.setFontSize(13);
        repetitionsNumberTF.setHorizontalAlignment(WebTextField.CENTER);
        add(repetitionsNumberTF);

        WebLabel firstStudentIDLbl = new WebLabel("ID первого студента");
        firstStudentIDLbl.setBounds(1035, 340, 200, 30);
        firstStudentIDLbl.setFontSize(13);
        add(firstStudentIDLbl);

        WebTextField firstStudentIDTF = new WebTextField("17341");
        firstStudentIDTF.setBounds(1220, 340, 60, 30);
        firstStudentIDTF.setFontSize(13);
        firstStudentIDTF.setHorizontalAlignment(WebTextField.CENTER);
        add(firstStudentIDTF);

        WebButton start = new WebButton("Старт");
        start.setBounds(1035, 380, 245, 30);
        start.setCursor(new Cursor(Cursor.HAND_CURSOR));
        start.setFontSize(13);
        add(start);

        WebButton combineFiles = new WebButton("Объединить файлы");
        combineFiles.setBounds(1035, 535, 245, 30);
        combineFiles.setCursor(new Cursor(Cursor.HAND_CURSOR));
        combineFiles.setFontSize(13);
        // add(combineFiles);

        //--------------------------------------------------------------------------------------------------------------
        Map<Integer, Boolean> checkedRows = new HashMap<>();
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == 0) {
                    if (!checkedRows.containsKey(row) || !checkedRows.get(row)) {
                        for (int i = 1; i <= 8; i++) {
                            tableModel.setValueAt(Boolean.TRUE, row, i);
                        }
                        checkedRows.put(row, Boolean.TRUE);
                    } else {
                        for (int i = 1; i <= 8; i++) {
                            tableModel.setValueAt(Boolean.FALSE, row, i);
                        }
                        checkedRows.put(row, Boolean.FALSE);
                    }
                }
                table.setModel(tableModel);
            }
        });

        start.addActionListener(e -> {
            List<GroupTest> gtList = new ArrayList<>();
            TableModel model = table.getModel();

            int studID = Integer.parseInt(firstStudentIDTF.getText());
            int repetitions = Integer.parseInt(repetitionsNumberTF.getText());

            for (int i = 0; i < model.getRowCount(); i++) {
                boolean r1a = (boolean) model.getValueAt(i, 1);
                boolean r1b = (boolean) model.getValueAt(i, 2);
                boolean r2a = (boolean) model.getValueAt(i, 3);
                boolean r2b = (boolean) model.getValueAt(i, 4);
                boolean za = (boolean) model.getValueAt(i, 5);
                boolean zb = (boolean) model.getValueAt(i, 6);
                boolean ea = (boolean) model.getValueAt(i, 7);
                boolean eb = (boolean) model.getValueAt(i, 8);

                boolean check = r1a || r1b || r2a || r2b || za || zb || ea || eb;

                if (check) {
                    GroupTest group = new GroupTest();

                    group.setStudID(studID);
                    group.setRepetitions(repetitions);
                    String subjectName = (String) model.getValueAt(i, 0);
                    group.setSubjectName(subjectName);

                    List<GroupItem> items = new ArrayList<>();

                    if (r1a) {
                        items.add(new GroupItem(
                                Testirovanie.Type.RATING_1,
                                Testirovanie.Part.A
                        ));
                    }

                    if (r1b) {
                        items.add(new GroupItem(
                                Testirovanie.Type.RATING_1,
                                Testirovanie.Part.B
                        ));
                    }

                    if (r2a) {
                        items.add(new GroupItem(
                                Testirovanie.Type.RATING_2,
                                Testirovanie.Part.A
                        ));
                    }

                    if (r2b) {
                        items.add(new GroupItem(
                                Testirovanie.Type.RATING_2,
                                Testirovanie.Part.B
                        ));
                    }

                    if (za) {
                        items.add(new GroupItem(
                                Testirovanie.Type.ZACHET,
                                Testirovanie.Part.A
                        ));
                    }

                    if (zb) {
                        items.add(new GroupItem(
                                Testirovanie.Type.ZACHET,
                                Testirovanie.Part.B
                        ));
                    }

                    if (ea) {
                        items.add(new GroupItem(
                                Testirovanie.Type.EXAM,
                                Testirovanie.Part.A
                        ));
                    }

                    if (eb) {
                        items.add(new GroupItem(
                                Testirovanie.Type.EXAM,
                                Testirovanie.Part.B
                        ));
                    }
                    group.setGroupItems(items);

                    gtList.add(group);
                }
            }

            if (gtList.size() > 0) {
                try {
                    final String filenamePrefix = "subjects/";
                    for (GroupTest gt : gtList) {
                        List<QuestionAnswers> qaList = testirovanie.getQuestionsAndAnswers(gt);
                        int timePrefix = (int) (new Date().getTime() % 1000000000);
                        testirovanie.save(filenamePrefix + timePrefix + " " + gt.getSubjectName(), qaList);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        combineFiles.addActionListener(e -> {
            // TODO: add implementation
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WebLookAndFeel.install();
            new TestirovanieGUI();
        });
    }

}
