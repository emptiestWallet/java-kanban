package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
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
        LocalDateTime startTime = task.getStartTime();
        Duration duration = task.getDuration();
        LocalDateTime endTime = task.getEndTime();

        return id + "," + type + "," + name + "," + status + "," + description + ","
                + (task instanceof Subtask ? ((Subtask) task).getEpicId() : "") + ","
                + (startTime != null ? startTime.toString() : "") + ","
                + (duration != null ? duration.toMinutes() : "") + ","
                + (endTime != null ? endTime.toString() : "");
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
        LocalDateTime startTime = null;
        Duration duration = null;
        LocalDateTime endTime = null;

        if (tokens.length > 6 && !tokens[6].isEmpty()) {
            startTime = LocalDateTime.parse(tokens[6]);
        }
        if (tokens.length > 7 && !tokens[7].isEmpty()) {
            long minutes = Long.parseLong(tokens[7]);
            duration = Duration.ofMinutes(minutes);
        }

        switch (type) {
            case TASK:
                task = new Task(name, description, status, TaskTypes.TASK, startTime, duration);
                break;
            case SUBTASK:
                if (tokens.length < 6) {
                    return null;
                }

                long epicId = Long.parseLong(tokens[5]);
                task = new Subtask(name, description, status, epicId, id, startTime, duration);
                break;
            case EPIC:
                task = new Epic(name, description, TaskStatus.NEW, startTime, duration);
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
