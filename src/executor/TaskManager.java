package executor;

import tasks.EpicTask;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {
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

    void updateEpicTaskStatus(EpicTask epicTask);
}
