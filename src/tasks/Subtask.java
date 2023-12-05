package tasks;

public class Subtask extends Task {
    public int epicId;
    public Subtask(String name, String description, String status, EpicTask epicTask) {
        super(name, description, status);
        this.epicId = epicTask.id;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
