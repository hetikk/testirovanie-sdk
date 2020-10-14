package ru.skgmigtu.testirovaniesdk.models;

import java.io.Serializable;
import java.util.Objects;

public class GroupItem implements Serializable, Comparable<GroupItem> {

    private Module module;
    private Part part;

    public GroupItem() {
    }

    public GroupItem(Module module, Part part) {
        this.module = module;
        this.part = part;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
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
        return module == groupItem.module &&
                part == groupItem.part;
    }

    @Override
    public int hashCode() {
        return Objects.hash(module, part);
    }

    @Override
    public String toString() {
        return String.format("TestItem { %s %s }", module, part);
    }

    @Override
    public int compareTo(GroupItem o) {
        int t = Integer.compare(module.value(), o.module.value());
        if (t != 0) {
            return Integer.compare(part.value(), o.part.value());
        }
        return t;
    }
}
