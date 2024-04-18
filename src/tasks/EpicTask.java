package tasks;

import java.util.ArrayList;

public class EpicTask extends Task {
    private final ArrayList<Integer> subtaskIds = new ArrayList<>();

    public EpicTask (String name, String description) {
        super(name, description, Statuses.NEW);
        this.type = TaskTypes.EPIC;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }
}
