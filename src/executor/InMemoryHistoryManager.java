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
    private int size = 0;

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
        size++;
        return node;
    }

    private List<Task> getTasks() {
        List<Task> taskHistory = new ArrayList<>();
        if (size != 0) {
            Node<Task> taskHead = head;
            while (taskHead != null) {
                taskHistory.add(taskHead.getData());
                taskHead = taskHead.getNext();
            }
        }
        return taskHistory;
    }

    private void removeNode(Node<Task> node) {
        if (size > 0 && node != null) {
            Node<Task> next = node.getNext();
            Node<Task> prev = node.getPrev();

            if (prev == null) {
                head = next;
                if (size > 1) {
                    head.setPrev(null);
                }
            } else if (next == null) {
                tail = prev;
                tail.setNext(null);
            } else {
                next.setPrev(prev);
                prev.setNext(next);
            }
            size--;
        }
    }

    @Override
    public void addHistory(Task task) {
        if (history.get(task.getId()) == null) {
            history.put(task.getId(), linkLast(task));
        } else {
            removeNode(history.get(task.getId()));
            history.put(task.getId(), linkLast(task));
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void removeHistory(int id) {
        if (history.containsKey(id)) {
            removeNode(history.get(id));
            history.remove(id);
        }
    }
}
