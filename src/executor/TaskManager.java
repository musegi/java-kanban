package executor;

import tasks.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    Task create(Task task);

    EpicTask create(EpicTask epicTask);

    Subtask create(Subtask subtask);

    Task update(Task task);

    EpicTask update(EpicTask epicTask);

    Subtask update(Subtask subtask);

    void deleteTaskById(Integer id);

    void deleteEpicTaskById(Integer id);

    void deleteSubtaskById(Integer id);

    ArrayList<Task> getTasksList();

    ArrayList<Task> getEpicTasksList();

    ArrayList<Task> getSubtasksList();

    void deleteTasks();

    void deleteEpicTasks();

    void deleteSubtasks();

    ArrayList<Subtask> getSubtaskListByEpicTaskId(Integer epicId);

    Task getTaskById(Integer id);

    EpicTask getEpicTaskById(Integer id);

    Subtask getSubtaskById(Integer id);

    List<Task> getPrioritizedTasks();
}
