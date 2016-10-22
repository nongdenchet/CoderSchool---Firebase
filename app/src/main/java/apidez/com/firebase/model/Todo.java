package apidez.com.firebase.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by nongdenchet on 2/2/16.
 */
public class Todo implements Serializable {
    private int id;
    private Priority priority;
    private String title;
    private Date dueDate;
    private boolean completed;

    public Todo(int id, String title, Date dueDate, boolean completed, Priority priority) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.completed = completed;
        this.priority = priority;
    }

    public void switchComplete() {
        completed = !completed;
    }

    public int getId() {
        return id;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getTitle() {
        return title;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Todo && ((Todo) obj).getId() == this.id;
    }

    /**
     * Builder class
     */
    public static class Builder {
        private static int ID = 0;
        private Priority priority;
        private String title;
        private Date dueDate;
        private boolean completed;

        public Builder(String title, Priority priority) {
            this.title = title;
            this.priority = priority;
        }

        public Builder dueDate(Date dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder completed(boolean completed) {
            this.completed = completed;
            return this;
        }

        public Todo build() {
            return new Todo(ID++, title, dueDate, completed, priority);
        }
    }
}
