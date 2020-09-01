package ru.skgmigtu.testirovaniesdk.models;

import ru.skgmigtu.testirovaniesdk.Testirovanie.Part;
import ru.skgmigtu.testirovaniesdk.Testirovanie.Type;

import java.io.Serializable;
import java.util.Objects;

public class GroupItem implements Serializable, Comparable<GroupItem> {

    private Type type;
    private Part part;

    public GroupItem() {
    }

    public GroupItem(Type type, Part part) {
        this.type = type;
        this.part = part;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupItem groupItem = (GroupItem) o;
        return type == groupItem.type &&
                part == groupItem.part;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, part);
    }

    @Override
    public String toString() {
        return String.format("TestItem { %s %s }", type, part);
    }

    @Override
    public int compareTo(GroupItem o) {
        int t = Integer.compare(type.value(), o.type.value());
        if (t != 0) {
            return Integer.compare(part.value(), o.part.value());
        }
        return t;
    }
}
