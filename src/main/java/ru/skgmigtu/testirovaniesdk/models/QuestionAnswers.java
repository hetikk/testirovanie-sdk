package ru.skgmigtu.testirovaniesdk.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class QuestionAnswers implements Serializable, Comparable<QuestionAnswers> {

    private Item question;
    private List<Item> answers;
    @SerializedName("right_answer")
    private Item rightAnswer;
    @SerializedName("multiple_choice")
    private boolean multipleChoice;

    public QuestionAnswers() {
    }

    public QuestionAnswers(Item question, List<Item> answers, Item rightAnswer, boolean multipleChoice) {
        this.question = question;
        setAnswers(answers);
        this.rightAnswer = rightAnswer;
        this.multipleChoice = multipleChoice;
    }

    public Item getQuestion() {
        return question;
    }

    public void setQuestion(Item question) {
        this.question = question;
    }

    public List<Item> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Item> answers) {
        Collections.sort(answers);
        this.answers = answers;
    }

    public Item getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(Item rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public boolean isMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(boolean multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    @Override
    public int compareTo(QuestionAnswers o) {
        return question.compareTo(o.question);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionAnswers that = (QuestionAnswers) o;
        return multipleChoice == that.multipleChoice &&
                question.equals(that.question) &&
                rightAnswer.equals(that.rightAnswer) &&
                answers.equals(that.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, rightAnswer, answers, multipleChoice);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("Вопрос: %s (id=%d, multiple_choice=%b)\n",
                question.getText(), question.getId(), multipleChoice));

        for (Item answer : answers) {
            builder.append("  · ").append(answer.toString()).append("\n");
        }

        builder.append("Правильный ответ: ").append(rightAnswer == null ? null : rightAnswer.toString());

        return builder.toString();
    }
}
