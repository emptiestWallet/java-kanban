package ru.practicum.task_tracker.tasks;

import java.util.Objects;

public class Subtask extends Task {
    private Long epicId;

    public Subtask(String name, String description, String status, Long epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Long getEpicId() {
        return epicId;
    }

    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), epicId); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Subtask anotherSubtask = (Subtask) obj;
        return Objects.equals(epicId, anotherSubtask.epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
