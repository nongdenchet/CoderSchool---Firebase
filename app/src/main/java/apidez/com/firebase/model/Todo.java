package apidez.com.firebase.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by nongdenchet on 2/2/16.
 */
public class Todo implements Serializable {
    private String id;
    private Priority priority = Priority.HIGH;
    private String title = "No title";
    private Date dueDate = new Date();
    private String uid;
    private boolean completed;

    public Todo() {
    }

    public Todo(Priority priority, String title, Date dueDate, String uid) {
        this.priority = priority;
        this.title = title;
        this.dueDate = dueDate;
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Todo && ((Todo) obj).getId().equals(this.id);
    }
}
