package ru.practicum.task_tracker.tasks;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.manager.Managers;
import ru.practicum.task_tracker.manager.TaskTracker;

public class EpicTest {
    private Epic epic;
    private TaskTracker taskTracker = Managers.getDefault();

    @BeforeEach
    void setUp() {
        epic = new Epic("Test Epic", "Epic Description", TaskStatus.NEW);
        taskTracker.addNewEpic(epic);
    }

    @Test
    void testEmptySubtask() {
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Epic должен быть NEW");
    }

    @Test
    void testAllSubtasksNew() {
        Subtask subtask1 = new Subtask("Subtask1", "Description1", TaskStatus.NEW, epic.getId(), epic.id);
        Subtask subtask2 = new Subtask("Subtask2", "Description2", TaskStatus.NEW, epic.getId(), epic.id);

        taskTracker.addNewSubtask(subtask1);
        taskTracker.addNewSubtask(subtask2);

        assertEquals(TaskStatus.NEW, epic.getStatus(), "Epic должен быть NEW для всех подзадач со статусом NEW");
    }

    @Test
    void testSubtasksInProgress() {
        Subtask subtask1 = new Subtask("Subtask1", "Description1", TaskStatus.NEW, epic.getId(), epic.id);
        Subtask subtask2 = new Subtask("Subtask2", "Description2", TaskStatus.IN_PROGRESS, epic.getId(), epic.id);

        taskTracker.addNewSubtask(subtask1);
        taskTracker.addNewSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Epic должен быть IN_PROGRESS при любой подзадаче со статусом IN_PROGRESS");
    }

    @Test
    void testAllSubtasksDone() {
        Subtask subtask1 = new Subtask("Subtask1", "Description1", TaskStatus.DONE, epic.getId(), epic.id);
        Subtask subtask2 = new Subtask("Subtask2", "Description2", TaskStatus.DONE, epic.getId(), epic.id);

        taskTracker.addNewSubtask(subtask1);
        taskTracker.addNewSubtask(subtask2);

        assertEquals(TaskStatus.DONE, epic.getStatus(), "Epic должен быть DONE для всех подзадач со статусом DONE");
    }

    @Test
    void testNewAndDoneSubtasks() {
        Subtask subtask1 = new Subtask("Subtask1", "Description1", TaskStatus.NEW, epic.getId(), epic.id);
        Subtask subtask2 = new Subtask("Subtask2", "Description2", TaskStatus.DONE, epic.getId(), epic.id);

        taskTracker.addNewSubtask(subtask1);
        taskTracker.addNewSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Epic должен быть DONE при любой подзадаче со статусом DONE");
    }
}