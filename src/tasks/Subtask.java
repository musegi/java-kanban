package tasks;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, Statuses status, EpicTask epicTask) {
        super(name, description, status);
        this.epicId = epicTask.id;
        this.type = TaskTypes.SUBTASK;
    }

    public Subtask(String name, String description, Statuses status, EpicTask epicTask,
                   String startTime, int duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicTask.id;
        this.type = TaskTypes.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return super.toString() + ',' +
                epicId;
    }
}
