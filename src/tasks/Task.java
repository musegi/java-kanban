package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    protected String name;
    protected String description;
    protected int id;
    protected Statuses status;
    protected TaskTypes type;
    protected LocalDateTime startTime = null;
    protected int duration = 0;

    public Task(String name, String description, Statuses status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskTypes.TASK;
    }

    public Task(String name, String description, Statuses status, String startTime, int duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskTypes.TASK;
        if (startTime != null && !startTime.equals("null")) {
            this.duration = duration;
            this.startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm dd.MM.yy"));
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Statuses getStatus() {
        return status;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plusMinutes(duration);
        }
        return null;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && type == task.type && Objects.equals(startTime, task.startTime);
    }

    @Override
    public String toString() {
            String toString = '\n' + Integer.toString(id) + ',' +
                    type + ',' +
                    name + ',' +
                    status + ',' +
                    description;
            if (duration != 0) {
                return toString + "," + startTime.format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")) + "," +
                        duration;
            } else {
                return toString + "," + "null" + "," + duration;
            }
    }
}
