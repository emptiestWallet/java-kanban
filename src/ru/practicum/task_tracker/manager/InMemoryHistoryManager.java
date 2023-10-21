package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Long, Node> taskNodeMap = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void addTaskToHistory(Task task) {
        if (task == null || task.getId() == null) {
            return;
        }

        long id = task.getId();
        remove(id);
        linkLast(task);
        taskNodeMap.put(id, last);
    }

    public void remove(long id) {
        Node node = taskNodeMap.remove(id);
        if (node == null) {
            return;
        }

        removeNode(node);
    }

    public void removeAll() {
        first = null;
        last = null;
        taskNodeMap.clear();
    }

    public ArrayList<Task> getTasks(){
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = first;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }

        return tasks;
    }

    public void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next == null) {
                last = node.prev;
            } else {
                node.next.prev = node.prev;
            }
        } else {
            first = node.next;
            if (first == null) {
                last = null;
            } else {
                first.prev = null;
            }
        }
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task, last, null);
        if (first == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }

        last = newNode;
    }

    public static class Node {
        Task task;
        Node next;
        Node prev;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }
    }
}