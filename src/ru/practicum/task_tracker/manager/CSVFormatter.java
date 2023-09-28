package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSVFormatter {
    private CSVFormatter() {}

    public static String toString(Task task) {
        long id = task.getId();
        TaskTypes type = task.getType();
        String name = task.getName();
        TaskStatus status = task.getStatus();
        String description = task.getDescription();

        return id + "," + type + "," + name + "," + status + "," + description + ","
                + (task instanceof Subtask ? ((Subtask) task).getEpicId() : "");
    }

    public static Task fromString(String taskStr) {
        String[] tokens = taskStr.split(",");

        if (tokens.length < 5) {
            return null;
        }

        long id = Long.parseLong(tokens[0]);
        TaskTypes type = TaskTypes.valueOf(tokens[1]);
        String name = tokens[2];
        TaskStatus status = TaskStatus.valueOf(tokens[3]);
        String description = tokens[4];
        Task task;

        switch (type) {
            case TASK:
                task = new Task(name, description, status, TaskTypes.TASK);
                break;
            case SUBTASK:
                if (tokens.length < 6) {
                    return null;
                }

                long epicId = Long.parseLong(tokens[5]);
                task = new Subtask(name, description, status, epicId, id);
                break;
            case EPIC:
                task = new Epic(name, description, TaskStatus.NEW);
                break;
            default:
                task = null;
                break;
        }

        if (task != null) {
            task.setId(id);
        }

        return task;
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < history.size(); i++) {
            builder.append(history.get(i).getId());

            if (i < history.size() - 1) {
                builder.append(",");
            }
        }

        return builder.toString();
    }

    public static List<Long> historyFromString(String historyStr) {
        if (historyStr == null || historyStr.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> history = new ArrayList<>();
        String[] tokens = historyStr.split(",");

        for (String token : tokens) {
            history.add(Long.parseLong(token));
        }

        return history;
    }
}
