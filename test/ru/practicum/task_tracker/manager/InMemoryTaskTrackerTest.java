package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.Test;

public class InMemoryTaskTrackerTest extends TaskTrackerTest {
    public final HistoryManager historyManager;

    public InMemoryTaskTrackerTest(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }
}