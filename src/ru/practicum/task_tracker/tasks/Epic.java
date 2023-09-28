package ru.practicum.task_tracker.tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    ArrayList<Long> subtaskIds;

    public Epic(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus.NEW, TaskTypes.EPIC);
        subtaskIds = new ArrayList<>();
    }

    public ArrayList<Long> getSubtaskIds() { return subtaskIds; }

    public void addSubtaskId(Long subtaskId) { subtaskIds.add(subtaskId); }

    public TaskTypes getType() {
        return TaskTypes.EPIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return super.equals(epic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, status, description);
    }

    @Override
    public String toString() {
        return "\nEpic{" +
                "subtaskIds=" + subtaskIds +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
