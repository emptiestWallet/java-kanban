package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Task;

import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();
    private static final int HISTORY_LIMIT = 10;

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public void addTaskToHistory(Task task) {
        if (task == null) {
            return;
        }

        if (history.size() >= HISTORY_LIMIT) {
            history.remove(0);
        }

        history.add(task);
    }
}
