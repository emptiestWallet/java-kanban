package ru.practicum.task_tracker.tasks;

import java.util.Objects;

public class Subtask extends Task {
    private Long epicId;

    public Subtask(String name, String description, TaskStatus status, Long epicId, Long id) {
        super(name, description, status, TaskTypes.SUBTASK);
        this.epicId = epicId;
        this.id = id;
    }

    public Long getEpicId() {
        return epicId;
    }

    public TaskTypes getType() {
        return TaskTypes.SUBTASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return super.equals(subtask) &&
                Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, status, description);
    }

    @Override
    public String toString() {
        return "\nSubtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
