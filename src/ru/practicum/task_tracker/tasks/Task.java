package ru.practicum.task_tracker.tasks;

import java.util.Objects;

public class Task {
    protected Long id;
    protected String name;
    protected String description;
    protected TaskStatus status;

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    @Override
    public int hashCode() { return Objects.hash(id, name, description, status); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if ((obj == null) || this.getClass() != obj.getClass()) return false;
        Task anotherTask = (Task) obj;
        return Objects.equals(id, anotherTask.id)
                && Objects.equals(name, anotherTask.name)
                && Objects.equals(description, anotherTask.description)
                && Objects.equals(status, anotherTask.status);
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
}
