package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.Test;

import java.io.File;

public class FileBackedTasksManagerTest extends TaskTrackerTest {
    protected final File dataFile;
    protected final HistoryManager historyManager;

    public FileBackedTasksManagerTest(File dataFile, HistoryManager historyManager) {
        this.dataFile = dataFile;
        this.historyManager = historyManager;
    }
}