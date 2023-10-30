package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskTrackerTest extends TaskTrackerTest<InMemoryTaskTracker> {
    @BeforeEach
    public void setUp() {
        taskTracker = new InMemoryTaskTracker();
    }

    @Test
    void testGetSubtaskByEpic() {
        Epic epic1 = new Epic("Epic 1", "Description", TaskStatus.NEW);
        Epic epic2 = new Epic("Epic 2", "Description", TaskStatus.NEW);

        taskTracker.addNewEpic(epic1);
        taskTracker.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", TaskStatus.NEW, epic1.getId(), 1L);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", TaskStatus.NEW, epic1.getId(), 2L);
        Subtask subtask3 = new Subtask("Subtask 3", "Description", TaskStatus.NEW, epic2.getId(), 3L);

        taskTracker.addNewSubtask(subtask1);
        taskTracker.addNewSubtask(subtask2);
        taskTracker.addNewSubtask(subtask3);

        List<Subtask> subtasksOfEpic1 = taskTracker.getSubtaskByEpic(epic1.getId());
        assertEquals(2, subtasksOfEpic1.size());
        assertTrue(subtasksOfEpic1.contains(subtask1));
        assertTrue(subtasksOfEpic1.contains(subtask2));

        List<Subtask> subtasksOfEpic2 = taskTracker.getSubtaskByEpic(epic2.getId());
        assertEquals(1, subtasksOfEpic2.size());
        assertTrue(subtasksOfEpic2.contains(subtask3));

        List<Subtask> subtasksOfInvalidEpic = taskTracker.getSubtaskByEpic(999L);
        assertTrue(subtasksOfInvalidEpic.isEmpty());
    }

    @Test
    void testGetTask() {
        Task task = new Task("Task 1", "Description", TaskStatus.NEW, TaskTypes.TASK);
        long taskId = taskTracker.addNewTask(task);

        Task retrievedTask = taskTracker.getTask(taskId);
        assertEquals(task, retrievedTask);

        Task retrievedTaskNull = taskTracker.getTask(null);
        assertNull(retrievedTaskNull);

        Task nonExistentTask = taskTracker.getTask(999L);
        assertNull(nonExistentTask);
    }

    @Test
    void testGetSubtask() {
        Epic epic = new Epic("Epic 1", "Description", TaskStatus.NEW);
        taskTracker.addNewEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Description", TaskStatus.NEW, epic.getId(), 1L);
        long subtaskId = taskTracker.addNewSubtask(subtask);

        Subtask retrievedSubtask = taskTracker.getSubtask(subtaskId);
        assertEquals(subtask, retrievedSubtask);

        Subtask retrievedSubtaskNull = taskTracker.getSubtask(null);
        assertNull(retrievedSubtaskNull);

        Subtask nonExistentSubtask = taskTracker.getSubtask(999L);
        assertNull(nonExistentSubtask);
    }

    @Test
    void testGetEpic() {
        Epic epic = new Epic("Epic 1", "Description", TaskStatus.NEW);
        long epicId = taskTracker.addNewEpic(epic);

        Epic retrievedEpic = taskTracker.getEpic(epicId);
        assertEquals(epic, retrievedEpic);

        Epic retrievedEpicNull = taskTracker.getEpic(null);
        assertNull(retrievedEpicNull);

        Epic nonExistentEpic = taskTracker.getEpic(999L);
        assertNull(nonExistentEpic);
    }

    @Test
    void testCalculateEpicStartDurationEndTime() {
        Epic epic = new Epic("Test Epic", "Epic Description", TaskStatus.NEW);
        taskTracker.addNewEpic(epic);

        LocalDateTime subtaskTime1 = LocalDateTime.of(2023, 1, 1, 8, 0);
        LocalDateTime subtaskTime2 = LocalDateTime.of(2023, 1, 1, 8,30);
        LocalDateTime subtaskTime3 = LocalDateTime.of(2023, 1, 1, 9,0);

        Duration subtaskDuration1 = Duration.ofMinutes(15);
        Duration subtaskDuration2 = Duration.ofMinutes(10);
        Duration subtaskDuration3 = Duration.ofMinutes(25);

        Subtask subtask1 = new Subtask("Subtask test", "Subtask Description", TaskStatus.NEW, epic.getId(), 1L,
                subtaskTime1, subtaskDuration1);
        Subtask subtask2 = new Subtask("Subtask test", "Subtask Description", TaskStatus.NEW, epic.getId(), 2L,
                subtaskTime2, subtaskDuration2);
        Subtask subtask3 = new Subtask("Subtask test", "Subtask Description", TaskStatus.NEW, epic.getId(), 3L,
                subtaskTime3, subtaskDuration3);

        taskTracker.addNewSubtask(subtask1);
        taskTracker.addNewSubtask(subtask2);
        taskTracker.addNewSubtask(subtask3);

        taskTracker.calculateEpicStartTime(epic.getId());
        taskTracker.calculateEpicDuration(epic.getId());
        taskTracker.calculateEpicEndTime(epic.getId());

        assertEquals(subtaskTime1, epic.getStartTime(), "Epic start time is not calculated correctly.");
        assertEquals(Duration.ofMinutes(50), epic.getDuration(), "Epic duration is not calculated correctly");
        assertEquals(subtaskTime3.plus(subtaskDuration3), epic.getEndTime(), "Epic end time is not calculated correctly");
    }

    @Test
    void testGetPrioritizedTasks() {
        LocalDateTime tasksTime = LocalDateTime.of(2023, 1, 1, 7, 30);
        Duration taskDuration = Duration.ofMinutes(20);

        Task task = new Task("Test Task", "Task Description", TaskStatus.NEW, TaskTypes.TASK, tasksTime,
                taskDuration);
        taskTracker.addNewTask(task);

        Epic epic = new Epic("Test Epic", "Epic Description", TaskStatus.NEW);
        taskTracker.addNewEpic(epic);

        LocalDateTime subtaskTime1 = LocalDateTime.of(2023, 1, 1, 8, 0);
        LocalDateTime subtaskTime2 = LocalDateTime.of(2023, 1, 1, 8,30);
        LocalDateTime subtaskTime3 = LocalDateTime.of(2023, 1, 1, 9,0);

        Duration subtaskDuration1 = Duration.ofMinutes(15);
        Duration subtaskDuration2 = Duration.ofMinutes(10);
        Duration subtaskDuration3 = Duration.ofMinutes(25);

        Subtask subtask1 = new Subtask("Subtask test", "Subtask Description", TaskStatus.NEW, epic.getId(), 1L,
                subtaskTime1, subtaskDuration1);
        Subtask subtask2 = new Subtask("Subtask test", "Subtask Description", TaskStatus.NEW, epic.getId(), 2L,
                subtaskTime2, subtaskDuration2);
        Subtask subtask3 = new Subtask("Subtask test", "Subtask Description", TaskStatus.NEW, epic.getId(), 3L,
                subtaskTime3, subtaskDuration3);

        taskTracker.addNewSubtask(subtask1);
        taskTracker.addNewSubtask(subtask2);
        taskTracker.addNewSubtask(subtask3);

        List<Task> prioritizedTasks = taskTracker.getPrioritizedTasks();

        assertEquals(task, prioritizedTasks.get(0));
        assertEquals(subtask1, prioritizedTasks.get(1));
        assertEquals(subtask2, prioritizedTasks.get(2));
        assertEquals(subtask3, prioritizedTasks.get(3));

        task.setStartTime(null);
        List<Task> updatedPrioritizedTasks = taskTracker.getPrioritizedTasks();
        assertEquals(subtask1, updatedPrioritizedTasks.get(0));
        assertEquals(subtask2, updatedPrioritizedTasks.get(1));
        assertEquals(subtask3, updatedPrioritizedTasks.get(2));
        assertEquals(task, updatedPrioritizedTasks.get(3));
    }
}