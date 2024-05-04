package executor;

import tasks.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    public HistoryManager historyManager = Managers.getDefaultHistory();
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public Set<Task> prioritizedTasks = new TreeSet<>(new StartTimeComparator());
    private int taskId = 1;

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task create(Task task) {
        if (isValid(task)) {
            task.setId(taskId++);
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
            return task;
        } else {
            System.out.println("Указано неверное время, задачи пересекаются");
            return null;
        }
    }

    @Override
    public EpicTask create(EpicTask epicTask) {
        epicTask.setId(taskId++);
        epicTasks.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    @Override
    public Subtask create(Subtask subtask) {
        if (isValid(subtask)) {
            if (epicTasks.containsKey(subtask.getEpicId())) {
                subtask.setId(taskId++);
                subtasks.put(subtask.getId(), subtask);
                EpicTask epicTask = getEpicTaskById(subtask.getEpicId());
                epicTask.getSubtaskIds().add(subtask.getId());
                updateEpicTaskStatus(epicTask);
                updateEpicTime(epicTask);
                prioritizedTasks.add(subtask);
                return subtask;
            }
            System.out.println("Указан неверный epicID, задача не создана");
            return null;
        }
        System.out.println("Указано неверное время, задачи пересекаются");
        return null;
    }

    @Override
    public Task update(Task task) {
        if (isValid(task)) {
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
            return task;
        } else {
            System.out.println("Указано неверное время, задачи пересекаются");
            return null;
        }
    }

    @Override
    public EpicTask update(EpicTask epicTask) {
        epicTasks.put(epicTask.getId(), epicTask);
        updateEpicTime(epicTask);
        return epicTask;
    }

    @Override
    public Subtask update(Subtask subtask) {
        if (isValid((subtask))) {
            subtasks.put(subtask.getId(), subtask);
            EpicTask epicTask = getEpicTaskById(subtask.getEpicId());
            epicTask.getSubtaskIds().add(subtask.getId());
            updateEpicTaskStatus(epicTask);
            updateEpicTime(epicTask);
            prioritizedTasks.add(subtask);
            return subtask;
        } else {
            System.out.println("Указано неверное время, задачи пересекаются");
            return null;
        }
    }

    @Override
    public void deleteTaskById(Integer id) {
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.removeHistory(id);
    }

    @Override
    public void deleteEpicTaskById(Integer id) {
        for (Integer subtaskId : epicTasks.get(id).getSubtaskIds()) {
            prioritizedTasks.remove(subtasks.get(subtaskId));
            subtasks.remove(subtaskId);
            historyManager.removeHistory(subtaskId);
        }
        epicTasks.remove(id);
        historyManager.removeHistory(id);

    }

    @Override
    public void deleteSubtaskById(Integer id) {
        EpicTask epicTask = getEpicTaskById(subtasks.get(id).getEpicId());
        epicTask.getSubtaskIds().remove(id);
        updateEpicTaskStatus(epicTask);
        updateEpicTime(epicTask);
        prioritizedTasks.remove(subtasks.get(id));
        subtasks.remove(id);
        historyManager.removeHistory(id);
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
        for (Integer taskId : tasks.keySet()) {
            historyManager.removeHistory(taskId);
            prioritizedTasks.remove(tasks.get(taskId));
        }
        tasks.clear();
    }

    @Override
    public void deleteEpicTasks() {
        for (Integer epicId : epicTasks.keySet()) {
            historyManager.removeHistory(epicId);
        }
        for (Integer subtaskId : subtasks.keySet()) {
            historyManager.removeHistory(subtaskId);
            prioritizedTasks.remove(subtasks.get(subtaskId));
        }
        epicTasks.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Integer subtaskId : subtasks.keySet()) {
            historyManager.removeHistory(subtaskId);
            prioritizedTasks.remove(subtasks.get(subtaskId));
        }
        subtasks.clear();
        for (EpicTask epicTask : epicTasks.values()) {
            epicTask.getSubtaskIds().clear();
            updateEpicTaskStatus(epicTask);
            updateEpicTime(epicTask);
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
        if (subtasks.containsKey(id)) {
            historyManager.addHistory(subtasks.get(id));
            return subtasks.get(id);
        }
        return null;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public boolean isValid(Task newTask) {
        if (newTask.getStartTime() == null) {
            return true;
        }
        return getPrioritizedTasks().stream()
                .filter(task -> task.getStartTime() != null)
                .filter(task -> task.getStartTime() != newTask.getStartTime())
                .noneMatch(task -> (task.getStartTime().isBefore(newTask.getEndTime()) &&
                        task.getEndTime().isAfter(newTask.getStartTime())));
    }

    private void updateEpicTaskStatus(EpicTask epicTask) {
        if (epicTask.getSubtaskIds().isEmpty()) {
            epicTask.setStatus(Statuses.NEW);
        } else {
            ArrayList<String> statusList = new ArrayList<>();
            for (Integer subtaskId : epicTask.getSubtaskIds()) {
                statusList.add(String.valueOf(getSubtaskById(subtaskId).getStatus()));
            }
            if (statusList.contains(Statuses.NEW) && !statusList.contains(Statuses.IN_PROGRESS)
                    && !statusList.contains(Statuses.DONE)) {
                epicTask.setStatus((Statuses.NEW));
            } else if (!statusList.contains(Statuses.NEW) && !statusList.contains(Statuses.IN_PROGRESS)
                    && statusList.contains(Statuses.DONE)) {
                epicTask.setStatus(Statuses.DONE);
            } else {
                epicTask.setStatus(Statuses.IN_PROGRESS);
            }
        }
    }

    private void updateEpicTime(EpicTask epicTask) {
        List<Integer> subtasksIdList = epicTask.getSubtaskIds();
        if (subtasksIdList.isEmpty()) {
            epicTask.setStartTime(null);
            epicTask.setDuration(0);
        }
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        int duration = 0;
        for (Integer subtaskId : subtasksIdList) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getDuration() == 0) {
                continue;
            } else if (startTime == null || subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
            if (endTime == null || subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }
            duration = duration + subtask.getDuration();
        }
        if (startTime != null) {
            epicTask.setStartTime(startTime);
            epicTask.setDuration(duration);
        }
    }
}
