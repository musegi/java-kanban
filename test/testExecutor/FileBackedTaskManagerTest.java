package testExecutor;

import executor.FileBackedTaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FileBackedTaskManagerTest {
    @Test
    void saveToFile() throws IOException {
        Path taskManagerTestFile = Files.createTempFile("taskManagerTest", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager("taskManagerTest.csv");
        Task task = new Task("task1", "task description", Statuses.NEW);
        EpicTask epic = new EpicTask("epic1", "task description");
        manager.create(task);
        manager.create(epic);
        manager.getTaskById(task.getId());
        manager.getEpicTaskById(epic.getId());
        Files.deleteIfExists(taskManagerTestFile);
    }

    @Test
    void loadFromFile() {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile("test/resources/testTaskData_load.csv");
        Task task = new Task( "task1", "task description", Statuses.NEW);
        EpicTask epic = new EpicTask( "epic1", "task description");
        Subtask subtask1 = new Subtask("subtask1", "task description 1", Statuses.NEW, epic);
        Subtask subtask2 = new Subtask("subtask2", "task description 1", Statuses.IN_PROGRESS, epic);
    }

    @Test
    void deleteTasksRemovedFromFile() throws IOException {
        Path taskManagerTestFile = Files.createTempFile("taskManagerTest", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager("taskManagerTest.csv");
        Task task = new Task("task1", "task description", Statuses.DONE,
                "12:00 01.02.02", 100);
        EpicTask epic = new EpicTask("epic1", "task description");
        manager.create(task);
        manager.create(epic);
        Subtask subtask1 = new Subtask("subtask1", "task description 1", Statuses.NEW, epic,
                "10:00 02.02.02", 150);
        Subtask subtask2 = new Subtask("subtask2", "task description 2", Statuses.NEW, epic,
                "14:00 02.02.02", 160);
        Subtask subtask3 = new Subtask("subtask3", "task description 3", Statuses.IN_PROGRESS, epic,
                "17:00 02.02.02", 160);
        Subtask subtask4 = new Subtask("subtask4", "task description 4", Statuses.DONE, epic,
                "07:00 02.02.02", 10);
        Task task2 = new Task("task2", "task description 2", Statuses.NEW,
                "11:00 01.02.02", 100);
        Task task3 = new Task("task3", "task description 3", Statuses.IN_PROGRESS,
                "10:00 01.02.02", 100);
        manager.create(subtask1);
        manager.create(subtask2);
        manager.create(subtask3);
        manager.create(subtask4);
        manager.create(task2);
        manager.create(task3);
        manager.getTaskById(task.getId());
        manager.getSubtaskById(subtask1.getId());
        manager.deleteTaskById(task.getId());
        Files.deleteIfExists(taskManagerTestFile);
    }
}
