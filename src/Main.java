import tasks.EpicTask;
import executor.Manager;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        manager.create(new Task("аа", "бб","NEW"));
        manager.create(new Task("вв", "гг","IN_PROGRESS"));
        manager.create(new Task("дд", "ее","NEW"));
        manager.create(new Task("ёё", "жж","NEW"));
        manager.create(new Task("зз", "ии","IN_PROGRESS"));
        System.out.println(manager.tasks);
        manager.create(new Task("йй", "кк","DONE"));
        manager.deleteTaskById(5);
        System.out.println(manager.tasks);
        System.out.println(manager.create(new EpicTask("йй", "кк")));

        System.out.println(manager.create(new Subtask("йй", "кк",
                "DONE", manager.getEpicTaskById(7))));
        System.out.println(manager.create(new Subtask("aa", "blin",
                "NEW", manager.getEpicTaskById(7))));
        manager.create(new Subtask("aавававаавыa", "blin",
                "IN_PROGRESS", manager.getEpicTaskById(7)));

        System.out.println(manager.epicTasks);
        System.out.println(manager.getSubtasksList());
        System.out.println(manager.getSubtaskById(8));
        System.out.println(manager.getTaskById(10));
        manager.create(new EpicTask("mama", "eto moya mama"));
        manager.create(new Subtask("family", "my family",
                "DONE", manager.getEpicTaskById(11)));
        manager.create(new Subtask("MAM", "my family",
                "DONE", manager.getEpicTaskById(11)));
        System.out.println();
        System.out.println(manager.getSubtaskListByEpicTaskId(7));
        System.out.println(manager.getSubtaskListByEpicTaskId(11));
        System.out.println();
        manager.deleteSubtaskById(8);
        System.out.println(manager.epicTasks);
        System.out.println(manager.subtasks);
        manager.deleteEpicTaskById(7);
        System.out.println(manager.epicTasks);
        System.out.println(manager.subtasks);
    }
}
