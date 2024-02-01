package executor;

import tasks.Task;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final Map<Integer, Node<Task>> history = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    private Node<Task> linkLast (Task task) {
        Node<Task> node = new Node<>(task);
        if (head == null) {
            head = node;
        } else if (tail == null) {
            tail = node;
            tail.setPrev(head);
            head.setNext(tail);
        } else {
            tail.setNext(node);
            node.setPrev(tail);
            tail = node;
            tail.setNext(null);
        }
        return node;
    }

    private List<Task> getTasks() {
        List<Task> taskHistory = new ArrayList<>();
        Node<Task> taskHead = head;
        while (taskHead != null) {
            taskHistory.add(taskHead.getData());
            taskHead = taskHead.getNext();
        }
        return taskHistory;
    }

    private void removeNode(int id) {
        Node<Task> node = history.remove(id);
        if (node == null) {
            return;
        }
            Node<Task> next = node.getNext();
            Node<Task> prev = node.getPrev();

            if (prev == null) {
                head = next;
                head.setPrev(null);
            } else if (next == null) {
                tail = prev;
                tail.setNext(null);
            } else {
                next.setPrev(prev);
                prev.setNext(next);
            }

    }

    @Override
    public void addHistory(Task task) {
        if (task == null) {
            return;
        }
        removeNode(task.getId());
        linkLast(task);
        history.put(task.getId(), tail);

    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void removeHistory(int id) {
        if (history.containsKey(id)) {
            removeNode(id);
        }
    }
}
