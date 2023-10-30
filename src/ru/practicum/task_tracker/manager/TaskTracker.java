package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.List;

public interface TaskTracker {
    List<Task> getAllTasks();

    List<Subtask> getAllSubtasks();

    List<Epic> getAllEpics();

    Task getTaskById(Long taskId);

    Subtask getSubtaskById(Long subtaskId);

    Epic getEpicById(Long epicId);

    Task getTask(Long taskId);

    Epic getEpic(Long epicId);

    Subtask getSubtask(Long subtaskId);

    long addNewTask(Task task);

    long addNewEpic(Epic epic);

    Long addNewSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpicStatus(Long epicId);

    void deleteTaskById(Long taskId);

    void deleteSubtaskById(Long subtaskId);

    void deleteEpicById(Long epicId);

    List<Subtask> getSubtaskByEpic(Long epicId);

    void calculateEpicStartTime(Long epicId);

    void calculateEpicDuration(Long epicId);

    void calculateEpicEndTime(Long epicId);
}
