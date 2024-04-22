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
        Task task = new Task("task1", "task description", Statuses.DONE);
        EpicTask epic = new EpicTask("epic1", "task description");
        manager.create(task);
        manager.create(epic);
        Subtask subtask1 = new Subtask("subtask1", "task description 1", Statuses.NEW, epic);
        manager.create(subtask1);
        manager.getTaskById(task.getId());
        manager.getSubtaskById(subtask1.getId());
        manager.deleteTaskById(task.getId());
        Files.deleteIfExists(taskManagerTestFile);
    }
}
