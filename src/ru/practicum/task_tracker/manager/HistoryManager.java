package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void addTaskToHistory(Task task);
    void remove(long id);
    List<Task> getHistory();
    void removeAll();
}
