# Testirovanie SDK
1. [Список вопросов и ответов](#question-answers-list)
2. [Список доступных предметов](#available-subjects)

## Список вопросов и ответов <a name="question-answers-list"></a>

```java
Testirovanie testirovanie = new Testirovanie();

int studID = 16001;                         // ID студента (номер зачетки), от имени которого совершается действие
String subjectName = "Intranet-технология"; // название дисциплины
Type type = Type.RATING_1;                  // тип сдачи (Рейтинг 1, Рейтинг 2, Зачет, Экзамен)
Part part = Part.A;                         // часть (А, В)

// или в виде одного объекта
LoginInformation li = new LoginInformation(
                studID,
                subjectName,
                type,
                part,
                repetitions
);

// можно даже так
List<LoginInformation> liList = new ArrayList<LoginInformation>() {{
    add(new LoginInformation(16001, subjectName, Type.RATING_1, Part.A, 3));
    add(new LoginInformation(16001, subjectName, Type.RATING_1, Part.B, 3));
    add(new LoginInformation(16001, subjectName, Type.RATING_2, Part.A, 3));
    add(new LoginInformation(16001, subjectName, Type.RATING_2, Part.B, 3));
}};

List<QuestionAnswers> qaList = testirovanie.getQuestionsAndAnswers(liList);

for (QuestionAnswers qa : qaList) {
    System.out.println(qa);
}
```

## Список доступных предметов <a name="available-subjects"></a>

```java
Testirovanie testirovanie = new Testirovanie();

int studID = 16001;                         // ID студента (номер зачетки), от имени которого совершается действие
Type type = Type.RATING_1;                  // тип сдачи (Рейтинг 1, Рейтинг 2, Зачет, Экзамен)
Part part = Part.A;                         // часть (А, В)

List<SubjectValue> svList = testirovanie.availableSubjects(studID, type, part);

for (SubjectValue sv : svList) {
    System.out.println(sv);
}
```
