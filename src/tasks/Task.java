package tasks;

public class Task {

    protected String name;
    protected String description;
    protected int id;
    protected Statuses status;
    protected TaskTypes type;

    public Task(String name, String description, Statuses status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskTypes.TASK;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Statuses getStatus() {
        return status;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }

        @Override
    public String toString() {
            return '\n' + Integer.toString(id) + ',' +
                    type + ',' +
                    name + ',' +
                    status + ',' +
                    description;
    }
}
