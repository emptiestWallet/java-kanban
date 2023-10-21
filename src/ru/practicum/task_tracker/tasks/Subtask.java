package ru.practicum.task_tracker.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private Long epicId;

    public Subtask(String name, String description, TaskStatus status, Long epicId, Long id, LocalDateTime startTime, Duration duration) {
        super(name, description, status, TaskTypes.SUBTASK);
        this.epicId = epicId;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }

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
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "\nSubtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
