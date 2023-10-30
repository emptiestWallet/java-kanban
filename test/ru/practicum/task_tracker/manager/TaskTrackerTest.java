package ru.practicum.task_tracker.manager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.tasks.*;

import java.util.List;

public abstract class TaskTrackerTest<T extends TaskTracker> {
    protected T taskTracker;

    @Test
    void testAddNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, TaskTypes.TASK);
        final long taskId = taskTracker.addNewTask(task);

        final Task savedTask = taskTracker.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskTracker.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void testAddNewSubtask() {
        Epic epic = new Epic("Test Epic", "Epic Description", TaskStatus.NEW);
        long epicId = taskTracker.addNewEpic(epic);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description", TaskStatus.NEW,
                epicId, epic.getId());
        final long subtaskId = taskTracker.addNewSubtask(subtask);

        final Task savedTask = taskTracker.getSubtask(subtaskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(subtask, savedTask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskTracker.getAllSubtasks();

        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void testAddNewEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", TaskStatus.NEW);
        final long epicId = taskTracker.addNewEpic(epic);

        final Task savedTask = taskTracker.getEpic(epicId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");

        final List<Epic> epics = taskTracker.getAllEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void testDeleteTaskById() {
        Task task = new Task("Test Task", "Task Description", TaskStatus.NEW, TaskTypes.TASK);
        long taskId = taskTracker.addNewTask(task);

        assertNotNull(taskTracker.getTask(taskId), "Задача не найдена в taskTracker до удаления.");
        taskTracker.deleteTaskById(taskId);
        assertNull(taskTracker.getTask(taskId), "Задача должна быть удалена из taskTracker.");
    }

    @Test
    void testDeleteSubtaskById() {
        Epic epic = new Epic("Test Epic", "Epic Description", TaskStatus.NEW);
        long epicId = taskTracker.addNewEpic(epic);

        Subtask subtask = new Subtask("Test Subtask", "Subtask Description", TaskStatus.NEW, epicId, epic.getId());
        long subtaskId = taskTracker.addNewSubtask(subtask);

        assertNotNull(taskTracker.getSubtask(subtaskId), "Подзадача не найдена в taskTracker до удаления.");

        taskTracker.deleteSubtaskById(subtaskId);

        assertNull(taskTracker.getSubtask(subtaskId), "Подзадача должна быть удалена из taskTracker.");

        Epic updatedEpic = taskTracker.getEpic(epicId);
        assertFalse(updatedEpic.getSubtaskIds().contains(subtaskId), "Подзадача должна быть удалена из эпика.");
    }

    @Test
    void testDeleteEpicById() {
        Epic epic = new Epic("Test Epic", "Epic Description", TaskStatus.NEW);
        long epicId = taskTracker.addNewEpic(epic);

        Subtask subtask1 = new Subtask("Test Subtask 1", "Subtask Description 1", TaskStatus.NEW, epicId, epic.getId());
        long subtaskId1 = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Test Subtask 2", "Subtask Description 2", TaskStatus.NEW, epicId, epic.getId());
        long subtaskId2 = taskTracker.addNewSubtask(subtask2);

        assertNotNull(taskTracker.getEpic(epicId), "Эпик не найден в taskTracker до удаления.");
        assertNotNull(taskTracker.getSubtask(subtaskId1), "Подзадача 1 не найдена в taskTracker до удаления.");
        assertNotNull(taskTracker.getSubtask(subtaskId2), "Подзадача 2 не найдена в taskTracker до удаления.");

        taskTracker.deleteEpicById(epicId);

        assertNull(taskTracker.getEpic(epicId), "Эпик должен быть удален из taskTracker.");
        assertNull(taskTracker.getSubtask(subtaskId1), "Подзадача 1 должна быть удалена из taskTracker.");
        assertNull(taskTracker.getSubtask(subtaskId2), "Подзадача 2 должна быть удалена из taskTracker.");
    }

    @Test
    void testUpdateTask() {
        Task task = new Task("Test Task", "Task Description", TaskStatus.NEW, TaskTypes.TASK);
        long taskId = taskTracker.addNewTask(task);

        Task updatedTask = new Task("Updated Task", "Updated Task Description", TaskStatus.IN_PROGRESS, TaskTypes.TASK);
        updatedTask.setId(taskId);

        assertNotNull(taskTracker.getTask(taskId), "Задача не найдена в taskTracker до обновления.");

        taskTracker.updateTask(updatedTask);

        Task savedTask = taskTracker.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена после обновления.");
        assertEquals(updatedTask, savedTask, "Задача не обновлена правильно.");
    }

    @Test
    void testUpdateSubtask() {
        Epic epic = new Epic("Test Epic", "Epic Description", TaskStatus.NEW);
        long epicId = taskTracker.addNewEpic(epic);

        Subtask subtask = new Subtask("Test Subtask", "Subtask Description", TaskStatus.NEW, epicId, epic.getId());
        long subtaskId = taskTracker.addNewSubtask(subtask);

        assertNotNull(taskTracker.getSubtask(subtaskId), "Подзадача не найдена в taskTracker до обновления.");

        subtask.setStatus(TaskStatus.IN_PROGRESS);
        taskTracker.updateSubtask(subtask);

        assertNotNull(subtask, "Подзадача не найдена после обновления.");
        assertEquals(TaskStatus.IN_PROGRESS, taskTracker.getSubtaskById(subtaskId).getStatus(), "Статус подзадачи не обновлен правильно");

        Epic updatedEpic = taskTracker.getEpic(epicId);
        assertNotNull(updatedEpic, "Эпик не найден после обновления подзадачи.");
        assertEquals(TaskStatus.IN_PROGRESS, updatedEpic.getStatus(), "Статус эпика не обновлен после обновления подзадачи.");
    }

    @Test
    void testUpdateEpicStatus() {
        Epic epic = new Epic("Test Epic", "Epic Description", TaskStatus.NEW);
        long epicId = taskTracker.addNewEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "Description1", TaskStatus.NEW, epicId, epic.getId());
        Subtask subtask2 = new Subtask("Subtask2", "Description2", TaskStatus.IN_PROGRESS, epicId, epic.getId());

        taskTracker.addNewSubtask(subtask1);
        taskTracker.addNewSubtask(subtask2);

        assertNotNull(taskTracker.getEpic(epicId), "Эпик не найден в taskTracker до обновления статуса.");

        taskTracker.updateEpicStatus(epicId);

        Epic savedEpic = taskTracker.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден после обновления статуса.");
        assertEquals(TaskStatus.IN_PROGRESS, savedEpic.getStatus(), "Статус эпика не обновлен правильно.");
    }
}
