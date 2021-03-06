# Testirovanie SDK
1. [Список вопросов и ответов](#question-answers-list)
   * [Прохождение одного теста](#question-answers-list-1)
   * [Прохождение одной группы тестов](#question-answers-list-2)
   * [Прохождение нескольких групп тестов](#question-answers-list-3)
2. [Список доступных предметов](#available-subjects)

## Список вопросов и ответов <a name="question-answers-list"></a>

<a name="question-answers-list-1"></a>
```java
Testirovanie testirovanie = new Testirovanie(BaseUrl.REMOTE);

int studID = 16001;                         // ID студента (номер зачетки), от имени которого совершается действие
String subjectName = "Intranet-технология"; // название дисциплины
Type testType = Type.RATING_1;                  // тип сдачи (Рейтинг 1, Рейтинг 2, Зачет, Экзамен)
Part testPart = Part.A;                         // часть (А, В)
int repetitions = 1;                        // количество повторений

List<QuestionAnswers> qaList = testirovanie.getQuestionsAndAnswers(studID, subjectName, testType, testPart, repetitions);

for (QuestionAnswers qa : qaList) {
    System.out.println(qa);
}
```

<a name="question-answers-list-2"></a>
```java
Testirovanie testirovanie = new Testirovanie(BaseUrl.REMOTE);

GroupTest gt = new GroupTest(
    16001,
    "Intranet-технология",
    new ArrayList<GroupItem>() {{
        add(new GroupItem(Type.RATING_1, Part.A));
        add(new GroupItem(Type.RATING_1, Part.B));
        add(new GroupItem(Type.RATING_2, Part.A));
        add(new GroupItem(Type.RATING_2, Part.B));
    }},
    1
);

List<QuestionAnswers> qaList = testirovanie.getQuestionsAndAnswers(gt);

for (QuestionAnswers qa : qaList) {
    System.out.println(qa);
}
```

<a name="question-answers-list-3"></a>
```java
Testirovanie testirovanie = new Testirovanie(BaseUrl.REMOTE);

List<GroupTest> gtList = new ArrayList<>();

GroupTest gt1 = new GroupTest(
    16001,
    "Intranet-технология",
    new ArrayList<GroupItem>() {{
        add(new GroupItem(Type.RATING_1, Part.A));
        add(new GroupItem(Type.RATING_1, Part.B));
    }},
    1
);
gtList.add(gt1);

GroupTest gt2 = new GroupTest(
    16001,
    "Патентные исследования",
    new ArrayList<GroupItem>() {{
        add(new GroupItem(Type.RATING_1, Part.A));
        add(new GroupItem(Type.RATING_1, Part.B));
        add(new GroupItem(Type.RATING_2, Part.A));
        add(new GroupItem(Type.RATING_2, Part.B));
    }},
    1
);
gtList.add(gt2);

GroupTest gt3 = new GroupTest(
    16001,
    "Теория принятия решений",
    new ArrayList<GroupItem>() {{
        add(new GroupItem(Type.RATING_1, Part.A));
        add(new GroupItem(Type.RATING_1, Part.B));
        add(new GroupItem(Type.RATING_2, Part.A));
        add(new GroupItem(Type.RATING_2, Part.B));
    }},
    1
);
gtList.add(gt3);

System.out.println("Списока вопросов и ответов:");
Map<String, List<QuestionAnswers>> qaMap = testirovanie.getQuestionsAndAnswers(gtList);
for (Map.Entry<String, List<QuestionAnswers>> entry : qaMap.entrySet()) {
    System.out.println("Предмет: " + entry.getKey());
    for (QuestionAnswers qa : entry.getValue()) {
        System.out.println(qa);
    }
    System.out.println();
}
```

## Список доступных предметов <a name="available-subjects"></a>

```java
Testirovanie testirovanie = new Testirovanie(BaseUrl.REMOTE);

int studID = 16001;         // ID студента (номер зачетки), от имени которого совершается действие
Type testType = Type.RATING_1;  // тип сдачи (Рейтинг 1, Рейтинг 2, Зачет, Экзамен)
Part testPart = Part.A;         // часть (А, В)

List<SubjectValue> svList = testirovanie.availableSubjects(studID, testType, testPart);

for (SubjectValue sv : svList) {
    System.out.println(sv);
}
```
