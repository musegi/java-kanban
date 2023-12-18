package executor;

import tasks.EpicTask;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    public HistoryManager historyManager = Managers.getDefaultHistory();
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int taskId = 1;

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task create(Task task){
        task.setId(taskId++);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public EpicTask create(EpicTask epicTask){
        epicTask.setId(taskId++);
        epicTasks.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    @Override
    public Subtask create(Subtask subtask){
        if (epicTasks.containsKey(subtask.getEpicId())) {
            subtask.setId(taskId++);
            subtasks.put(subtask.getId(), subtask);
            EpicTask epicTask = getEpicTaskById(subtask.getEpicId());
            epicTask.getSubtaskIds().add(subtask.getId());
            updateEpicTaskStatus(epicTask);
            return subtask;
        }
        return null;
    }

    @Override
    public Task update(Task task){
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public EpicTask update(EpicTask epicTask){
        epicTasks.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    @Override
    public Subtask update(Subtask subtask){
        subtasks.put(subtask.getId(), subtask);
        EpicTask epicTask = getEpicTaskById(subtask.getEpicId());
        epicTask.getSubtaskIds().add(subtask.getId());
        updateEpicTaskStatus(epicTask);
        return subtask;
    }

    @Override
    public void deleteTaskById(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicTaskById(Integer id) {
        for (Integer subtaskId : epicTasks.get(id).getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
        epicTasks.remove(id);

    }

    @Override
    public void deleteSubtaskById(Integer id) {
        EpicTask epicTask = getEpicTaskById(subtasks.get(id).getEpicId());
        epicTask.getSubtaskIds().remove(id);
        updateEpicTaskStatus(epicTask);
        subtasks.remove(id);
    }

    @Override
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Task> getEpicTasksList() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public ArrayList<Task> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpicTasks() {
        epicTasks.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        for (EpicTask epicTask : epicTasks.values()) {
            epicTask.getSubtaskIds().clear();
            updateEpicTaskStatus(epicTask);
        }
    }

    @Override
    public ArrayList<Subtask> getSubtaskListByEpicTaskId(Integer epicId) {
        EpicTask epicTask = getEpicTaskById(epicId);
        ArrayList<Subtask> subtasksInEpicTask = new ArrayList<>();
        for (Integer subtaskId : epicTask.getSubtaskIds()) {
            subtasksInEpicTask.add(getSubtaskById(subtaskId));
        }
        return subtasksInEpicTask;
    }

    @Override
    public Task getTaskById(Integer id) {
        if (tasks.containsKey(id)) {
            historyManager.addHistory(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public EpicTask getEpicTaskById(Integer id) {
        if (epicTasks.containsKey(id)) {
            historyManager.addHistory(epicTasks.get(id));
            return epicTasks.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        if (subtasks.containsKey(id)){
            historyManager.addHistory(subtasks.get(id));
            return subtasks.get(id);
        }
        return null;
    }

    private void updateEpicTaskStatus(EpicTask epicTask){
        if (epicTask.getSubtaskIds().isEmpty()){
            epicTask.setStatus(Statuses.NEW);
        } else {
            ArrayList<String> statusList = new ArrayList<>();
            for (Integer subtaskId : epicTask.getSubtaskIds()) {
                statusList.add(String.valueOf(getSubtaskById(subtaskId).getStatus()));
            } if (statusList.contains(Statuses.NEW) && !statusList.contains(Statuses.IN_PROGRESS)
                    && !statusList.contains(Statuses.DONE)) {
                epicTask.setStatus((Statuses.NEW));
            } else if (!statusList.contains(Statuses.NEW) && !statusList.contains(Statuses.IN_PROGRESS)
                    && statusList.contains(Statuses.DONE)){
                epicTask.setStatus(Statuses.DONE);
            } else {
                epicTask.setStatus(Statuses.IN_PROGRESS);
            }
        }
    }
}
