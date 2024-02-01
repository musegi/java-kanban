import tasks.EpicTask;
import executor.InMemoryTaskManager;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        manager.create(new Task("аа", "бб",Statuses.NEW));
        manager.create(new Task("вв", "гг",Statuses.IN_PROGRESS));
        manager.create(new Task("дд", "ее",Statuses.NEW));
        manager.create(new Task("ёё", "жж",Statuses.NEW));
        manager.create(new Task("зз", "ии",Statuses.IN_PROGRESS));
        System.out.println(manager.tasks);
        manager.create(new Task("йй", "кк",Statuses.DONE));
        manager.deleteTaskById(5);
        System.out.println(manager.tasks);
        System.out.println(manager.create(new EpicTask("йй", "кк")));

        System.out.println(manager.create(new Subtask("йй", "кк",
                Statuses.DONE, manager.getEpicTaskById(7))));
        System.out.println(manager.create(new Subtask("aa", "blin",
                Statuses.NEW, manager.getEpicTaskById(7))));
        manager.create(new Subtask("aавававаавыa", "blin",
                Statuses.IN_PROGRESS, manager.getEpicTaskById(7)));

        System.out.println(manager.epicTasks);
        System.out.println(manager.getSubtasksList());
        System.out.println(manager.getSubtaskById(8));
        System.out.println(manager.getTaskById(10));
        manager.create(new EpicTask("mama", "eto moya mama"));
        manager.create(new Subtask("family", "my family",
                Statuses.DONE, manager.getEpicTaskById(11)));
        manager.create(new Subtask("MAM", "my family",
                Statuses.DONE, manager.getEpicTaskById(11)));
        System.out.println();
        System.out.println(manager.getSubtaskListByEpicTaskId(7));
        System.out.println(manager.getSubtaskListByEpicTaskId(11));
        System.out.println();
        System.out.println("история 1: " + manager.getHistory());
        manager.deleteSubtaskById(8);
        System.out.println("история 2: " + manager.getHistory());
        System.out.println(manager.epicTasks);
        System.out.println(manager.subtasks);
        manager.getEpicTaskById(7);
        manager.getTaskById(4);
        manager.getEpicTaskById(7);
        manager.getEpicTaskById(7);
        System.out.println(manager.getHistory());
        manager.deleteEpicTasks();
        System.out.println("история: " + manager.getHistory());
        manager.deleteTaskById(4);
        System.out.println(manager.getHistory());
    }
}
