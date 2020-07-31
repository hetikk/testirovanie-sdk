package ru.skgmigtu.testirovaniesdk.models;

import java.io.Serializable;
import java.util.Objects;

public class Item implements Serializable, Comparable<Item> {

    private int id;
    private String text;

    public Item() {
    }

    public Item(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int compareTo(Item o) {
        return text.compareTo(o.text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id &&
                text.equals(item.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }

    @Override
    public String toString() {
        return String.format("%s (id=%d)", text, id);
    }
}
