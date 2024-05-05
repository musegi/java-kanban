import executor.FileBackedTaskManager;
import tasks.EpicTask;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile("src/save.csv");

        manager.create(new Task("аа", "бб",Statuses.NEW, "09:30 25.07.24", 30));
        manager.create(new Task("вв", "гг",Statuses.IN_PROGRESS, "10:30 25.07.24", 60));
        manager.create(new Task("дд", "ее",Statuses.NEW,"12:50 26.07.24", 75));
        manager.create(new Task("ёё", "жж",Statuses.NEW, "14:52 27.08.24",40));
        manager.create(new Task("зз", "ии",Statuses.IN_PROGRESS, "15:59 27.08.24",50));
        System.out.println(manager.tasks);
        manager.create(new Task("йй", "кк",Statuses.DONE));
        System.out.println(manager.tasks);
        manager.create(new Task("сос", "помогите",Statuses.NEW,
                "08:45 20.07.24", 120));
        System.out.println(manager.create(new EpicTask("йй", "кк", null, 0)));
        System.out.println(manager.create(new Subtask("aa", "blin",
                Statuses.NEW, manager.getEpicTaskById(8), "10:24 28.08.24",15)));
        manager.create(new Subtask("aавававаавыa", "blin",
                Statuses.IN_PROGRESS, manager.getEpicTaskById(8), "15:46 23.08.24",20));
        System.out.println(manager.epicTasks);
        System.out.println(manager.getSubtasksList());
        System.out.println();
        System.out.println();
        System.out.println(manager.getPrioritizedTasks());
        System.out.println();
        System.out.println();
        System.out.println("история 1: " + manager.getHistory());
        System.out.println(manager.epicTasks);
        System.out.println(manager.subtasks);
        System.out.println("история: " + manager.getHistory());

    }
}
