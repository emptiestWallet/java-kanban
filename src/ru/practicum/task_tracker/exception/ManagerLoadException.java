package ru.practicum.task_tracker.exception;

public class ManagerLoadException extends RuntimeException {
    public ManagerLoadException (String message, Throwable cause) {
        super(message, cause);
    }
}
