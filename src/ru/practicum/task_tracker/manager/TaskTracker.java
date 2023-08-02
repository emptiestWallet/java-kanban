package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskTracker {
    private HashMap<Long, Task> tasks = new HashMap<>();
    private HashMap<Long, Subtask> subtasks = new HashMap<>();
    private HashMap<Long, Epic> epics = new HashMap<>();
    private long generatorId = 0;

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public Task getTaskById(Long taskId) {
        return tasks.get(taskId);
    }

    public Subtask getSubtaskById(Long subtaskId) {
        return subtasks.get(subtaskId);
    }

    public Epic getEpicById(Long epicId) {
        return epics.get(epicId);
    }

    public long addNewTask(Task task) {
        long id = generateId();
        task.setId(id);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public long addNewEpic(Epic epic) {
        long id = generateId();
        epic.setId(id);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public Long addNewSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return null;
        }

        long id = generateId();
        subtask.setId(id);
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(subtask.getEpicId());

        return subtask.getId();
    }

    public void updateTask(Task task) {
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            System.out.println("Task с id" + task.getId() + " отсутствует");
            return;
        }

        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        Subtask savedSubtask = subtasks.get(subtask.getId());
        if (savedSubtask == null) {
            System.out.println("Subtask с id" + subtask.getId() + " отсутствует");
            return;
        }

        updateEpicStatus(subtask.getEpicId());
    }

    private void updateEpicStatus(Long epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Long> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus("NEW");
            return;
        }

        String status = null;
        for (long subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);

            if (status == null) {
                status = subtask.getStatus();
                continue;
            }

            if (status.equals(subtask.getStatus())
            && !status.equals("IN_PROGRESS")) {
                continue;
            }

            epic.setStatus("IN_PROGRESS");
            return;
        }

        epic.setStatus(status);
    }

    public void deleteAllTasks() {
        tasks.clear();
        subtasks.clear();
        epics.clear();
    }

    public void deleteTaskById(Long taskId) {
        Task task = getTaskById(taskId);
        if (task == null) {
            return;
        }

        tasks.remove(taskId);
    }

    public void deleteSubtaskById(Long subtaskId) {
        Subtask subtask = getSubtaskById(subtaskId);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtaskIds().remove(subtaskId);
            }

            subtasks.remove(subtaskId);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    public void deleteEpicById(Long epicId) {
        Epic epic = epics.remove(epicId);
        if (epic == null) {
            return;
        }

        List<Long> subtasksToRemove = new ArrayList<>();
        for (Long subtaskId : subtasks.keySet()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getEpicId().equals(epicId)) {
                subtasksToRemove.add(subtaskId);
            }
        }

        for (Long subtaskId : subtasksToRemove) {
            subtasks.remove(subtaskId);
        }
    }

    public List<Subtask> getSubtaskByEpic(Long epicId) {
        List<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId().equals(epicId)) {
                subtasksOfEpic.add(subtask);
            }
        }

        return subtasksOfEpic;
    }

    private long generateId() {
        return generatorId++;
    }
}
