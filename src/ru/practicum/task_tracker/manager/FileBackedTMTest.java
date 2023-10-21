package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.exception.ManagerSaveException;
import ru.practicum.task_tracker.tasks.*;

import java.io.File;

public class FileBackedTMTest {
    public static void main(String[] args) {
        File file = new File("tasks.csv");
        HistoryManager historyManager = /*new InMemoryHistoryManager()*/ Managers.getDefaultHistory();
        FileBackedTasksManager tasksManager1 = new FileBackedTasksManager(file/*, historyManager*/);

        Task task1 = new Task("TaskName1", "TaskDescription1", TaskStatus.NEW, TaskTypes.TASK);
        Task task2 = new Task("TaskName2", "TaskDescription2", TaskStatus.NEW, TaskTypes.TASK);
        Epic epic1 = new Epic("EpicName1", "EpicDescription1", TaskStatus.NEW);
        Subtask subtask1 = new Subtask("SubtaskName1", "SubtaskDescription1", TaskStatus.NEW, 2L, 1L);

        tasksManager1.addNewTask(task1);
        tasksManager1.addNewTask(task2);
        tasksManager1.addNewEpic(epic1);
        tasksManager1.addNewSubtask(subtask1);

        historyManager.addTaskToHistory(task2);
        historyManager.addTaskToHistory(subtask1);

        try {
            tasksManager1.save();
        } catch (ManagerSaveException e) {
            System.err.println("Ошибка при сохранении данных " + e.getMessage());
        }

        FileBackedTasksManager tasksManager2 = FileBackedTasksManager.loadFromFile(file, historyManager);

        if (tasksManager1.equals(tasksManager2)) {
            System.out.println("Данные восстановлены корректно");
        } else {
            System.out.println("Ошибка при восстановлении данных");
        }

        System.out.println(tasksManager1.tasks.equals(tasksManager2.tasks));
        System.out.println(tasksManager1.subtasks.equals(tasksManager2.subtasks));
        System.out.println(tasksManager1.epics.equals(tasksManager2.epics));
        System.out.println(tasksManager1.getHistoryManager().equals(tasksManager2.getHistoryManager()));
    }
}
