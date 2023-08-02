package ru.practicum.task_tracker.tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    ArrayList<Long> subtaskIds;

    public Epic(String name, String description) {
        super(name, description, "NEW");
        subtaskIds = new ArrayList<>();
    }

    public ArrayList<Long> getSubtaskIds() { return subtaskIds; }

    public void addSubtaskId(Long subtaskId) { subtaskIds.add(subtaskId); }

    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), subtaskIds); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Epic anotherEpic = (Epic) obj;
        return Objects.equals(subtaskIds, anotherEpic.subtaskIds);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtaskIds +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
