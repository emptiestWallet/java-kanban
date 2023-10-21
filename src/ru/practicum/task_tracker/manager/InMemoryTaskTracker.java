package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;
import ru.practicum.task_tracker.tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskTracker implements TaskTracker {
    public final HashMap<Long, Task> tasks = new HashMap<>();
    public final HashMap<Long, Subtask> subtasks = new HashMap<>();
    public final HashMap<Long, Epic> epics = new HashMap<>();
    public long generatorId = 0;
    public final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskTracker that = (InMemoryTaskTracker) o;
        return generatorId == that.generatorId &&
                tasks.equals(that.tasks) &&
                subtasks.equals(that.subtasks) &&
                epics.equals(that.epics) &&
                historyManager.getHistory().equals(that.historyManager.getHistory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasks, subtasks, epics, generatorId, historyManager);
    }

    /*public InMemoryTaskTracker(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }*/

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTaskById(Long taskId) {
        return tasks.get(taskId);
    }

    @Override
    public Subtask getSubtaskById(Long subtaskId) {
        return subtasks.get(subtaskId);
    }

    @Override
    public Epic getEpicById(Long epicId) {
        return epics.get(epicId);
    }

    private long generateId() {
        return generatorId++;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

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
            throw new IllegalArgumentException("Эпик с указанным ID не существует");
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

    @Override
    public void updateEpicStatus(Long epicId) {
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

    @Override
    public void calculateEpicStartTime(Long epicId) {
        List<LocalDateTime> test = new ArrayList<>();
        if (epics.values().isEmpty()) {
            return;
        }
        for (Epic epic : epics.values()) {
            if (Objects.equals(epic.getId(), epicId)) {
                List<Subtask> epicSubtasks = getSubtaskByEpic(epic.getId());
                if (epicSubtasks.isEmpty()) {
                    return;
                }
                for (Subtask subtask : epicSubtasks) {
                    if (subtask.getStartTime() != null) {
                        test.add(subtask.getStartTime());
                        LocalDateTime min = Collections.min(test);
                        epic.setStartTime(min);
                        calculateEpicDuration(epic.getId());
                    }
                }
            }
        }
    }

    @Override
    public void calculateEpicDuration(Long epicId) {
        Duration duration = Duration.ZERO;
        if (epics.values().isEmpty()) {
            return;
        }
        for (Epic epic : epics.values()) {
            if (Objects.equals(epic.getId(), epicId)) {
                List<Subtask> epicSubtasks = getSubtaskByEpic(epic.getId());
                if (epicSubtasks.isEmpty()) {
                    return;
                }
                for (Subtask subtask : epicSubtasks) {
                    if (subtask.getDuration() == null) {
                        epic.setDuration(duration);
                        return;
                    }
                    duration = duration.plus(subtask.getDuration());
                    epic.setDuration(duration);
                }
            }
        }
    }

    @Override
    public void calculateEpicEndTime(Long epicId) {
        List<LocalDateTime> test = new ArrayList<>();
        if (epics.values().isEmpty()) {
            return;
        }
        for (Epic epic : epics.values()) {
            if (Objects.equals(epic.getId(), epicId)) {
                List<Subtask> epicSubtasks = getSubtaskByEpic(epic.getId());
                if (epicSubtasks.isEmpty()) {
                    if (epic.getStartTime() != null && epic.getDuration() != null) {
                        LocalDateTime endTime = epic.getStartTime().plus(epic.getDuration());
                        epic.setEndTime(endTime);
                    }
                    return;
                }
                for (Subtask subtask : epicSubtasks) {
                    if (subtask.getEndTime() != null) {
                        test.add(subtask.getEndTime());
                        LocalDateTime max = Collections.max(test);
                        epic.setEndTime(max);
                    }
                }
            }
        }
    }
}
