import executor.InMemoryHistoryManager;
import tasks.EpicTask;
import executor.InMemoryTaskManager;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        taskManager.create(new Task("аа", "бб",Statuses.NEW));
        taskManager.create(new Task("вв", "гг",Statuses.IN_PROGRESS));
        taskManager.create(new Task("дд", "ее",Statuses.NEW));
        taskManager.create(new Task("ёё", "жж",Statuses.NEW));
        taskManager.create(new Task("зз", "ии",Statuses.IN_PROGRESS));
        System.out.println(taskManager.tasks);
        taskManager.create(new Task("йй", "кк",Statuses.DONE));
        taskManager.deleteTaskById(5);
        System.out.println(taskManager.tasks);
        System.out.println(taskManager.create(new EpicTask("йй", "кк")));

        System.out.println(taskManager.create(new Subtask("йй", "кк",
                Statuses.DONE, taskManager.getEpicTaskById(7))));
        System.out.println(taskManager.create(new Subtask("aa", "blin",
                Statuses.NEW, taskManager.getEpicTaskById(7))));
        taskManager.create(new Subtask("aавававаавыa", "blin",
                Statuses.IN_PROGRESS, taskManager.getEpicTaskById(7)));

        System.out.println(taskManager.epicTasks);
        System.out.println(taskManager.getSubtasksList());
        System.out.println(taskManager.getSubtaskById(8));
        System.out.println(taskManager.getTaskById(10));
        taskManager.create(new EpicTask("mama", "eto moya mama"));
        taskManager.create(new Subtask("family", "my family",
                Statuses.DONE, taskManager.getEpicTaskById(11)));
        taskManager.create(new Subtask("MAM", "my family",
                Statuses.DONE, taskManager.getEpicTaskById(11)));
        System.out.println();
        System.out.println(taskManager.getSubtaskListByEpicTaskId(7));
        System.out.println(taskManager.getSubtaskListByEpicTaskId(11));
        System.out.println();
        taskManager.deleteSubtaskById(8);
        System.out.println(taskManager.epicTasks);
        System.out.println(taskManager.subtasks);
        taskManager.getEpicTaskById(7);
        taskManager.getEpicTaskById(7);
        taskManager.getEpicTaskById(7);
        taskManager.getEpicTaskById(7);
        System.out.println(historyManager.getHistory());
    }
}
