package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;
import ru.practicum.task_tracker.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskTracker implements TaskTracker {
    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final HashMap<Long, Subtask> subtasks = new HashMap<>();
    private final HashMap<Long, Epic> epics = new HashMap<>();
    private long generatorId = 0;
    private final HistoryManager historyManager;

    public InMemoryTaskTracker(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getAllTasks() { return new ArrayList<>(tasks.values()); }

    @Override
    public List<Subtask> getAllSubtasks() { return new ArrayList<>(subtasks.values()); }

    @Override
    public List<Epic> getAllEpics() { return new ArrayList<>(epics.values()); }

    @Override
    public Task getTaskById(Long taskId) { return tasks.get(taskId); }

    @Override
    public Subtask getSubtaskById(Long subtaskId) { return subtasks.get(subtaskId); }

    @Override
    public Epic getEpicById(Long epicId) { return epics.get(epicId); }

    private long generateId() { return generatorId++; }

    @Override
    public long addNewTask(Task task) {
        long id = generateId();
        task.setId(id);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public long addNewEpic(Epic epic) {
        long id = generateId();
        epic.setId(id);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
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

    @Override
    public void updateTask(Task task) {
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            System.out.println("Task с id" + task.getId() + " отсутствует");
            return;
        }

        tasks.put(task.getId(), task);
    }

    @Override
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
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        TaskStatus status = null;
        for (long subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);

            if (status == null) {
                status = subtask.getStatus();
                continue;
            }

            if (status.equals(subtask.getStatus())
            && !status.equals(TaskStatus.IN_PROGRESS)) {
                continue;
            }

            epic.setStatus(TaskStatus.IN_PROGRESS);
            return;
        }

        epic.setStatus(status);
    }

    public void deleteAllTasks() {
        tasks.clear();
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteTaskById(Long taskId) {
        Task task = getTaskById(taskId);
        if (task == null) {
            return;
        }

        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteSubtaskById(Long subtaskId) {
        Subtask subtask = subtasks.remove(subtaskId);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtaskIds().remove(subtaskId);
            }

            updateEpicStatus(subtask.getEpicId());

            historyManager.remove(subtaskId);
        }
    }

    @Override
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
            historyManager.remove(subtaskId);
        }

        historyManager.remove(epicId);
    }

    @Override
    public List<Subtask> getSubtaskByEpic(Long epicId) {
        List<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId().equals(epicId)) {
                subtasksOfEpic.add(subtask);
            }
        }

        return subtasksOfEpic;
    }

    @Override
    public Task getTask(Long taskId) {
        Task task = tasks.get(taskId);

        if (task != null) {
            historyManager.addTaskToHistory(task);
        }

        return task;
    }

    @Override
    public Epic getEpic(Long epicId) {
        Epic epic = epics.get(epicId);

        if (epic != null) {
            historyManager.addTaskToHistory(epic);
        }

        return epic;
    }

    @Override
    public Subtask getSubtask(Long subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);

        if (subtask != null) {
            historyManager.addTaskToHistory(subtask);
        }

        return subtask;
    }
}
