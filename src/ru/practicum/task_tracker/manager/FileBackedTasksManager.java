package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.exception.ManagerSaveException;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;
import ru.practicum.task_tracker.tasks.TaskTypes;

import java.io.*;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskTracker {
    private final File dataFile;
    //private final HistoryManager historyManager = Managers.getDefaultHistory() ;

    public FileBackedTasksManager(File dataFile/*, HistoryManager historyManager*/) {
        //super(historyManager);
        this.dataFile = dataFile;
        //this.historyManager = historyManager;
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter("tasks.csv")) {
            fileWriter.write("id,type,name,status,description,epic\n");
            List<Task> tasks = getAllTasks();
            for (Task task : tasks) {
                fileWriter.write(CSVFormatter.toString(task) + "\n");
            }

            List<Epic> epics = getAllEpics();
            for (Epic epic : epics) {
                fileWriter.write(CSVFormatter.toString(epic) + "\n");
            }

            List<Subtask> subtasks = getAllSubtasks();
            for (Subtask subtask : subtasks) {
                fileWriter.write(CSVFormatter.toString(subtask) + "\n");
            }

            fileWriter.write("\n");

            fileWriter.write(CSVFormatter.historyToString(historyManager));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileBackedTasksManager loadFromFile(File file, HistoryManager historyManager) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file/*, historyManager*/);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                Task task = CSVFormatter.fromString(line);
                if (task.getType().equals(TaskTypes.TASK)) {
                    manager.addNewTask(task);
                }
                if (task.getType().equals(TaskTypes.SUBTASK)) {
                    manager.addNewSubtask((Subtask) task);
                }
                if (task.getType().equals(TaskTypes.EPIC)) {
                    manager.addNewEpic((Epic) task);
                }
            }

            reader.readLine();

            line = reader.readLine();
            List<Long> history = CSVFormatter.historyFromString(line);
            history.forEach(manager::markTaskAsViewed);

        } catch (IOException e) {
            throw new ManagerSaveException("Failed to load manager data from file.", e);
        }

        return manager;
    }

    public void markTaskAsViewed(Long taskId) {
        Task task = getTaskById(taskId);
        if (task != null) {
            task.setViewed(true);
        }
    }

    @Override
    public long addNewTask(Task task) {
        long id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public Long addNewSubtask(Subtask subtask) {
        Long id = super.addNewSubtask(subtask);
        if (id != null) {
            save();
        }
        return id;
    }

    @Override
    public long addNewEpic(Epic epic) {
        long id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public void deleteTaskById(Long taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteSubtaskById(Long subtaskId) {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    @Override
    public void deleteEpicById(Long epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpicStatus(Long epicId) {
        super.updateEpicStatus(epicId);
        save();
    }
}
