package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.practicum.task_tracker.tasks.*;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    private static HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testAddRemoveGetFromHistory() {
        Task task1 = new Task("TaskName1", "TaskDescription1", TaskStatus.NEW, TaskTypes.TASK);
        task1.setId(1L);
        Task task2 = new Task("TaskName2", "TaskDescription2", TaskStatus.NEW, TaskTypes.TASK);
        task2.setId(2L);
        Task task3 = new Task("TaskName3", "TaskDescription3", TaskStatus.NEW, TaskTypes.TASK);
        task3.setId(3L);

        historyManager.addTaskToHistory(task1);
        historyManager.addTaskToHistory(task2);
        historyManager.addTaskToHistory(task3);

        historyManager.remove(2L);

        assertEquals(2, historyManager.getHistory().size());
        assertNull(historyManager.getHistory().stream()
                .filter(task -> task.getId() == 2L)
                .findFirst()
                .orElse(null));

        historyManager.removeAll();
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    void testEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void testTaskDuplication() {
        Task task = new Task("TaskName", "TaskDescription", TaskStatus.NEW, TaskTypes.TASK);
        task.setId(1L);

        historyManager.addTaskToHistory(task);
        historyManager.addTaskToHistory(task);

        assertEquals(1, historyManager.getHistory().size());
    }
}
