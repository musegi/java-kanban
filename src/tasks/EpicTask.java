package tasks;

import java.util.ArrayList;

public class EpicTask extends Task {
    private final ArrayList<Integer> subtaskIds = new ArrayList<>();

    public EpicTask (String name, String description) {
        super(name, description, Statuses.NEW);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "subtaskIds=" + subtaskIds +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }
}
