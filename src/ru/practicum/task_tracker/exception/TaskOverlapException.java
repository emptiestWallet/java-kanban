package ru.practicum.task_tracker.exception;

public class TaskOverlapException extends RuntimeException {
    public TaskOverlapException() {
        super("Обнаружено пересечение задач");
    }

    public TaskOverlapException(String message) {
        super(message);
    }

    public TaskOverlapException(String message, Throwable cause) {
        super(message, cause);
    }
}
