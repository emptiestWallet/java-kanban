package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.HistoryManager;
import ru.practicum.task_tracker.manager.Managers;
import ru.practicum.task_tracker.manager.TaskTracker;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;
import ru.practicum.task_tracker.tasks.TaskStatus;

public class Main {
    public static void main(String[] args) {
        TaskTracker taskTracker = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Epic epic1 = new Epic("Подарок другу", "Купить подарок другу на день рождения");
        Epic epic2 = new Epic("Путешествие в Японию", "Совершить долгожданную поездку");

        taskTracker.addNewEpic(epic1);
        taskTracker.addNewEpic(epic2);

        Subtask subtask1ForEpic1 = new Subtask("Выбор подарка", "Подобрать в интернет-магазине подарок",
                TaskStatus.DONE, epic1.getId());
        Subtask subtask2ForEpic1 = new Subtask("Вручение подарка",
                "Назначить место встречи и вручить подарок", TaskStatus.IN_PROGRESS, epic1.getId());
        Subtask subtask3ForEpic1 = new Subtask("Отпраздновать с другом",
                "Сходить в кафе и отпраздновать ДР", TaskStatus.NEW, epic1.getId());

        taskTracker.addNewSubtask(subtask1ForEpic1);
        taskTracker.addNewSubtask(subtask2ForEpic1);
        taskTracker.addNewSubtask(subtask3ForEpic1);

        historyManager.addTaskToHistory(epic1);
        historyManager.addTaskToHistory(subtask1ForEpic1);
        historyManager.addTaskToHistory(subtask2ForEpic1);
        historyManager.addTaskToHistory(subtask3ForEpic1);

        System.out.println("\nИстория просмотра:");
        System.out.println(historyManager.getHistory());

        historyManager.addTaskToHistory(epic2);
        historyManager.addTaskToHistory(subtask2ForEpic1);
        historyManager.addTaskToHistory(epic1);

        System.out.println("\nИстория просмотра с измененным порядком задач и добавлением пустого эпика:");
        System.out.println(historyManager.getHistory());

        taskTracker.deleteSubtaskById(subtask1ForEpic1.getId());

        System.out.println("\nИстория просмотра после удаления подзадачи:");
        System.out.println(historyManager.getHistory());

        taskTracker.deleteEpicById(epic1.getId());

        System.out.println("\nИстория просмотра после удаления эпика:");
        System.out.println(historyManager.getHistory());
    }
}
