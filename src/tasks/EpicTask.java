package tasks;

import java.util.ArrayList;

public class EpicTask extends Task {
    public ArrayList<Integer> subtaskIds = new ArrayList<>();
    public EpicTask (String name, String description) {
        super(name, description, "NEW");
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
}
