package ru.practicum.task_tracker.tasks;

import ru.practicum.task_tracker.manager.InMemoryTaskTracker;
import ru.practicum.task_tracker.manager.Managers;
import ru.practicum.task_tracker.manager.TaskTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java.time.Duration;
import java.time.LocalDateTime;

public class Epic extends Task {
    ArrayList<Long> subtaskIds;
    TaskTracker taskTracker = Managers.getDefault();
    private LocalDateTime endTime;

    public Epic(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus.NEW, TaskTypes.EPIC);
        subtaskIds = new ArrayList<>();
    }

    public Epic(String name, String description, TaskStatus taskStatus, LocalDateTime startTime, Duration duration) {
        super(name, description, taskStatus.NEW, TaskTypes.EPIC);
        subtaskIds = new ArrayList<>();
        this.startTime = startTime;
        this.duration = duration;
        this.getEndTime();
    }

    public ArrayList<Long> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(Long subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public TaskTypes getType() {
        return TaskTypes.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds)
                && Objects.equals(taskTracker, epic.taskTracker)
                && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds, taskTracker, endTime);
    }

    @Override
    public String toString() {
        return "\nEpic{" +
                "subtaskIds=" + subtaskIds +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
