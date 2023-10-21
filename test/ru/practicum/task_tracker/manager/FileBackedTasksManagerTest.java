package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.exception.ManagerSaveException;
import ru.practicum.task_tracker.tasks.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskTrackerTest<FileBackedTasksManager> {
    File file;

    @BeforeEach
    public void setUp() {
        taskTracker = new FileBackedTasksManager(file);
        file = new File("tasks.csv");
    }

    @AfterEach
    void clean() {
        taskTracker.getHistoryManager().removeAll();
    }

    @Test
    public void testSaveLoad() {
        Task task1 = new Task("TaskName1", "TaskDescription1", TaskStatus.NEW, TaskTypes.TASK);
        Epic epic1 = new Epic("EpicName1", "EpicDescription1", TaskStatus.NEW);

        taskTracker.addNewTask(task1);
        taskTracker.addNewEpic(epic1);

        Subtask subtask1 = new Subtask("SubtaskName1", "SubtaskDescription1", TaskStatus.NEW, epic1.getId(), 3L);
        taskTracker.addNewSubtask(subtask1);

        try {
            taskTracker.save();
        } catch (ManagerSaveException e) {
            System.err.println("Ошибка при сохранении данных " + e.getMessage());
        }

        FileBackedTasksManager loadedManager = taskTracker.loadFromFile(file, taskTracker.getHistoryManager());

        Task loadedTask = loadedManager.getTask(task1.getId());
        Epic loadedEpic = loadedManager.getEpic(epic1.getId());
        Subtask loadedSubtask = loadedManager.getSubtask(subtask1.getId());

        assertNotNull(loadedTask, "Задача не загружена из файла.");
        assertEquals(task1, loadedTask, "Загруженная задача отличается от оригинала.");

        assertNotNull(loadedEpic, "Эпик не загружен из файла.");
        assertEquals(epic1, loadedEpic, "Загруженный эпик отличается от оригинала.");

        assertNotNull(loadedSubtask, "Подзадача не загружена из файла.");
        assertEquals(subtask1, loadedSubtask, "Загруженная подзадача отличается от оригинала.");
    }

    @Test
    void testMarkTaskAsViewed() {
        Task task = new Task("Test Task", "Task Description", TaskStatus.NEW, TaskTypes.TASK);
        long taskId = taskTracker.addNewTask(task);
        taskTracker.save();

        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile(file, taskTracker.getHistoryManager());

        loadedManager.markTaskAsViewed(taskId);

        Task viewedTask = loadedManager.getTask(taskId);
        assertTrue(viewedTask.isViewed(), "Задача не помечена как просмотренная.");
    }

    @Test
    void testSaveWithEmptyTaskList() {
        FileBackedTasksManager taskManager = new FileBackedTasksManager(file);

        taskManager.save();

        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile(file, taskManager.getHistoryManager());

        List<Task> tasks = loadedManager.getAllTasks();
        assertTrue(tasks.isEmpty(), "Список задач не пустой после загрузки из пустого файла.");
    }

    @Test
    void testSaveLoadEpicWithoutSubtasks() {
        Epic epic = new Epic("Test Epic", "Epic Description", TaskStatus.NEW);
        taskTracker.addNewEpic(epic);

        try {
            taskTracker.save();
        } catch (ManagerSaveException e) {
            System.err.println("Ошибка при сохранении данных " + e.getMessage());
        }

        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile(file, taskTracker.getHistoryManager());

        assertTrue(loadedManager.getAllEpics().size() == 1, "Список задач не содержит 1 эпик после загрузки.");
        assertTrue(loadedManager.getSubtaskByEpic(epic.getId()).isEmpty(), "Список подзадач не пустой после загрузки.");
    }

    @Test
    void testSaveLoadEmptyHistory() {
        Task task1 = new Task("TaskName1", "TaskDescription1", TaskStatus.NEW, TaskTypes.TASK);
        taskTracker.addNewTask(task1);
        taskTracker.getHistoryManager().addTaskToHistory(task1);

        try {
            taskTracker.save();
        } catch (ManagerSaveException e) {
            System.err.println("Ошибка при сохранении данных " + e.getMessage());
        }

        taskTracker.getHistoryManager().remove(task1.getId());

        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile(file, taskTracker.getHistoryManager());

        assertTrue(loadedManager.getHistoryManager().getHistory().isEmpty(), "Список истории не пустой после загрузки.");
    }

    @Test
    void testSaveLoadEmptyTasks() {
        try {
            taskTracker.save();
        } catch (ManagerSaveException e) {
            System.err.println("Ошибка при сохранении данных " + e.getMessage());
        }

        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile(file, taskTracker.getHistoryManager());

        assertTrue(loadedManager.getAllTasks().isEmpty(), "Список задач не пустой после загрузки.");
    }
}