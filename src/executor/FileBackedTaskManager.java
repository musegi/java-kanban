package executor;

import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final String file;
    private static final String FILE_HEADER  = "id,type,name,status,description,epic";

    public FileBackedTaskManager(String file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(String file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        String tasksInFile = parsingFile(file);
        if (tasksInFile == null) {
            return fileBackedTaskManager;
        }
        String[] taskLine = tasksInFile.split("\n");
        List<Integer> taskInHistory = historyFromString(file);
        for (String taskString : taskLine) {
            if (taskString.equals(FILE_HEADER)) {
                continue;
            } else if (taskString.isEmpty()) {
                break;
            }
            Task task = fileBackedTaskManager.fromString(taskString);
            if (taskInHistory != null) {
                for (Integer id : taskInHistory) {
                    if (task.getId() == id) {
                        fileBackedTaskManager.historyManager.addHistory(task);
                        break;
                    }
                }
            }
        }
        return fileBackedTaskManager;
    }

    private Task fromString(String line) {
        String[] part = line.split(",");
        final int id = Integer.parseInt(part[0]);
        final TaskTypes type = TaskTypes.valueOf(part[1]);
        final String name = part[2];
        final Statuses status = Statuses.valueOf(part[3]);
        final String description = part[4];
        switch (type) {
            case TASK:
                Task task = new Task(name, description, status);
                task.setId(id);
                create(task);
                return task;

            case EPIC:
                EpicTask epicTask = new EpicTask(name, description);
                epicTask.setId(id);
                create(epicTask);
                return epicTask;

            case SUBTASK:
                final int epic = Integer.parseInt(part[5]);
                Subtask subtask = new Subtask(name, description, status, epicTasks.get(epic));
                subtask.setId(id);
                create(subtask);
                return subtask;
        }
        return null;
    }

    private static String parsingFile(String file) {
        try {
            return Files.readString(Path.of(file));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    static String historyToString(HistoryManager manager) {
        List<Task> tasksInHistory = manager.getHistory();
        StringBuilder historyInFile = new StringBuilder();
        for (Task task : tasksInHistory) {
            historyInFile.append(task.getId());
            historyInFile.append(",");
        }
        if (!historyInFile.toString().isEmpty()) {
            historyInFile.deleteCharAt(historyInFile.length() - 1);
        }
        return historyInFile.toString();
    }

    private static List<Integer> historyFromString(String value) {
        String[] historyArray = parsingFile(value).split("\n");
        String stringOfHistory = historyArray[historyArray.length - 1];
        if (stringOfHistory.isEmpty()) {
            return null;
        }
        String[] lineHistory = stringOfHistory.split(",");
        List<Integer> historyList = new ArrayList<>();
        for (String line : lineHistory) {
            historyList.add(Integer.parseInt(line));
        }
        return historyList;
    }

    private void save() {
        try {
            Writer fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(FILE_HEADER);
            for (Task task : tasks.values()) {
                bufferedWriter.write(task.toString());
            }
            for (EpicTask epicTask : epicTasks.values()) {
                bufferedWriter.write(epicTask.toString());
            }
            for (Subtask subtask : subtasks.values()) {
                bufferedWriter.write(subtask.toString());
            }
            bufferedWriter.write("\n\n");
            bufferedWriter.write(historyToString(historyManager));
            bufferedWriter.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задачи");
        }
    }

    @Override
    public Task create(Task task) {
        super.create(task);
        save();
        return task;
    }

    @Override
    public EpicTask create(EpicTask epicTask) {
        super.create(epicTask);
        save();
        return epicTask;
    }

    @Override
    public Subtask create(Subtask subtask) {
        super.create(subtask);
        save();
        return subtask;
    }

    @Override
    public Task update(Task task) {
        super.update(task);
        save();
        return task;
    }

    @Override
    public EpicTask update(EpicTask epicTask) {
        super.update(epicTask);
        save();
        return epicTask;
    }

    @Override
    public Subtask update(Subtask subtask) {
        super.update(subtask);
        save();
        return subtask;
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicTaskById(Integer id) {
        super.deleteEpicTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpicTasks() {
        super.deleteEpicTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public EpicTask getEpicTaskById(Integer id) {
        EpicTask epicTask = super.getEpicTaskById(id);
        save();
        return epicTask;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }


}
