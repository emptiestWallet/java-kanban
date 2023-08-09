package ru.practicum.task_tracker.manager;

public class Managers {
    public static TaskTracker getDefault() {
        return new InMemoryTaskTracker();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
