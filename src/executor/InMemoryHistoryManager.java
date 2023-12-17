package executor;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_SIZE = 10;
    private static final LinkedList<Task> history = new LinkedList<>();

    @Override
    public void addHistory(Task task) {
        history.add(task);
        if (history.size() > HISTORY_SIZE) {
            history.removeFirst();
        }
    }
    @Override
    public List<Task> getHistory() {
        return history;
    }
}
