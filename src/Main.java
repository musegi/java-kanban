import executor.FileBackedTaskManager;
import tasks.EpicTask;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile("src/save.csv");

        manager.create(new Task("аа", "бб",Statuses.NEW));
        manager.create(new Task("вв", "гг",Statuses.IN_PROGRESS));
        manager.create(new Task("дд", "ее",Statuses.NEW));
        manager.create(new Task("ёё", "жж",Statuses.NEW));
        manager.create(new Task("зз", "ии",Statuses.IN_PROGRESS));
        System.out.println(manager.tasks);
        manager.create(new Task("йй", "кк",Statuses.DONE));
        System.out.println(manager.tasks);
        System.out.println(manager.create(new EpicTask("йй", "кк")));
        System.out.println(manager.create(new Subtask("aa", "blin",
                Statuses.NEW, manager.getEpicTaskById(7))));
        manager.create(new Subtask("aавававаавыa", "blin",
                Statuses.IN_PROGRESS, manager.getEpicTaskById(7)));
        System.out.println(manager.epicTasks);
        System.out.println(manager.getSubtasksList());
        System.out.println();
        System.out.println();
        System.out.println("история 1: " + manager.getHistory());
        System.out.println(manager.epicTasks);
        System.out.println(manager.subtasks);
        System.out.println("история: " + manager.getHistory());
    }
}
