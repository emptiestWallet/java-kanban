package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.TaskTracker;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskTracker taskTracker = new TaskTracker();

        Task task1 = new Task("Утренняя пробежка", "Совершить утреннюю пробежку в 9 часов", "NEW");
        Task task2 = new Task("Покупка зубной щетки", "Сходить в гипермаркет за новой зубной щеткой",
                "IN_PROGRESS");

        taskTracker.addNewTask(task1);
        taskTracker.addNewTask(task2);

        Epic epic1 = new Epic("Подарок другу", "Купить подарок другу на день рождения");
        Epic epic2 = new Epic("Путешествие в Японию", "Совершить долгожданную поездку");

        taskTracker.addNewEpic(epic1);
        taskTracker.addNewEpic(epic2);

        Subtask subtask1ForEpic1 = new Subtask("Выбор подарка", "Подобрать в интернет-магазине подарок",
                "DONE", epic1.getId());
        Subtask subtask2ForEpic1 = new Subtask("Вручение подарка", "Назначить место встречи и вручить подарок",
                "IN_PROGRESS", epic1.getId());
        Subtask subtask1ForEpic2 = new Subtask("Покупка билетов", "Купить билеты на сайте",
                "NEW", epic2.getId());

        taskTracker.addNewSubtask(subtask1ForEpic1);
        taskTracker.addNewSubtask(subtask2ForEpic1);
        taskTracker.addNewSubtask(subtask1ForEpic2);

        System.out.println("\nСписок задач:");
        for (Task task : taskTracker.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("\nСписок эпиков:");
        for (Epic epic : taskTracker.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("\nСписок подзадач:");
        for (Subtask subtask : taskTracker.getAllSubtasks()) {
            System.out.println(subtask);
        }

        task1.setStatus("DONE");
        subtask2ForEpic1.setStatus("DONE");

        taskTracker.updateTask(task1);
        taskTracker.updateSubtask(subtask2ForEpic1);

        System.out.println("\nОбновленный список задач:");
        for (Task task : taskTracker.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("\nОбновленный список эпиков:");
        for (Epic epic : taskTracker.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("\nОбновленный список подзадач:");
        for (Subtask subtask : taskTracker.getAllSubtasks()) {
            System.out.println(subtask);
        }

        taskTracker.deleteTaskById(task1.getId());
        taskTracker.deleteEpicById(epic1.getId());

        System.out.println("\nСписок задач после выборочного удаления:");
        for (Task task : taskTracker.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("\nСписок эпиков после выборочного удаления:");
        for (Epic epic : taskTracker.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("\nСписок подзадач после выборочного удаления:");
        for (Subtask subtask : taskTracker.getAllSubtasks()) {
            System.out.println(subtask);
        }
    }
}
