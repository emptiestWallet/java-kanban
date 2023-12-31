package ru.practicum.task_tracker.manager;

public class Managers {
    private static final HistoryManager defaultHistoryManager = new InMemoryHistoryManager();
    private static final TaskTracker defaultTaskManager = new InMemoryTaskTracker();

    public static TaskTracker getDefault() {
        return defaultTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return defaultHistoryManager;
    }
}