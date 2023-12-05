package executor;

import tasks.EpicTask;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();
    int taskId = 1;

    public Task create(Task task){
        task.setId(taskId++);
        tasks.put(task.getId(), task);
        return task;
    }

    public EpicTask create(EpicTask epicTask){
        epicTask.setId(taskId++);
        epicTasks.put(epicTask.getId(), epicTask);
        return epicTask;
    }

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

    public Task update(Task task){
        tasks.put(task.getId(), task);
        return task;
    }

    public EpicTask update(EpicTask epicTask){
        epicTasks.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    public Subtask update(Subtask subtask){
        subtasks.put(subtask.getId(), subtask);
        EpicTask epicTask = getEpicTaskById(subtask.getEpicId());
        epicTask.getSubtaskIds().add(subtask.getId());
        updateEpicTaskStatus(epicTask);
        return subtask;
    }

    public void deleteTaskById(Integer id) {
        tasks.remove(id);
    }

    public void deleteEpicTaskById(Integer id) {
        for (Integer subtaskId : epicTasks.get(id).getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
        epicTasks.remove(id);

    }

    public void deleteSubtaskById(Integer id) {
        EpicTask epicTask = getEpicTaskById(subtasks.get(id).getEpicId());
        epicTask.getSubtaskIds().remove(id);
        updateEpicTaskStatus(epicTask);
        subtasks.remove(id);
    }

    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Task> getEpicTasksList() {
        return new ArrayList<>(epicTasks.values());
    }

    public ArrayList<Task> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpicTasks() {
        epicTasks.clear();
        subtasks.clear();
    }

    public void deleteSubtasks() {
        subtasks.clear();
        for (EpicTask epicTask : epicTasks.values()) {
            epicTask.getSubtaskIds().clear();
            updateEpicTaskStatus(epicTask);
        }
    }

    public ArrayList<Subtask> getSubtaskListByEpicTaskId(Integer epicId) {
        EpicTask epicTask = getEpicTaskById(epicId);
        ArrayList<Subtask> subtasksInEpicTask = new ArrayList<>();
        for (Integer subtaskId : epicTask.getSubtaskIds()) {
            subtasksInEpicTask.add(getSubtaskById(subtaskId));
        }
        return subtasksInEpicTask;
    }

    public Task getTaskById(Integer id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        return null;
    }

    public EpicTask getEpicTaskById(Integer id) {
        if (epicTasks.containsKey(id)) {
            return epicTasks.get(id);
        }
        return null;
    }

    public Subtask getSubtaskById(Integer id) {
        if (subtasks.containsKey(id)){
            return subtasks.get(id);
        }
        return null;
    }

    private void updateEpicTaskStatus(EpicTask epicTask){
        if (epicTask.getSubtaskIds().isEmpty()){
            epicTask.setStatus("NEW");
        } else {
            ArrayList<String> statusList = new ArrayList<>();
            for (Integer subtaskId : epicTask.getSubtaskIds()) {
                statusList.add(getSubtaskById(subtaskId).getStatus());
            } if (statusList.contains("NEW") && !statusList.contains("IN_PROGRESS")
                    && !statusList.contains("DONE")) {
                epicTask.setStatus(("NEW"));
            } else if (!statusList.contains("NEW") && !statusList.contains("IN_PROGRESS")
                    && statusList.contains("DONE")){
                epicTask.setStatus("DONE");
            } else {
                epicTask.setStatus("IN_PROGRESS");
            }
        }
    }
}
