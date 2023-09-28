package ru.practicum.task_tracker.tasks;

import java.util.Objects;

public class Task {
    protected Long id;
    protected String name;
    protected TaskTypes type;
    protected String description;
    protected TaskStatus status;
    protected boolean viewed;

    public Task(String name, String description, TaskStatus status, TaskTypes type) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(type, task.type) &&
                Objects.equals(name, task.name) &&
                Objects.equals(status, task.status) &&
                Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, status, description);
    }

    @Override
    public String toString() {
        return "\nTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public TaskStatus getStatus() { return status; }

    public void setStatus(TaskStatus status) { this.status = status; }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskTypes getType() {
        return TaskTypes.TASK;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
