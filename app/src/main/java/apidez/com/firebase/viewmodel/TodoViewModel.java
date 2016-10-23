package apidez.com.firebase.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableInt;
import android.text.format.DateFormat;
import android.view.View;

import apidez.com.firebase.model.Priority;
import apidez.com.firebase.model.Todo;

/**
 * Created by nongdenchet on 2/8/16.
 */
public class TodoViewModel extends BaseObservable {
    private final String NO_DUE_DATE = "No due date";
    private Todo mTodo;
    public ObservableInt actionVisibility = new ObservableInt(View.GONE);
    public ObservableInt dividerVisibility = new ObservableInt(View.VISIBLE);

    public TodoViewModel(Todo todo) {
        this.mTodo = todo;
    }

    public Todo getTodo() {
        return mTodo;
    }

    public void setTodo(Todo todo) {
        this.mTodo = todo;
    }

    public void resetState() {
        actionVisibility.set(View.GONE);
        dividerVisibility.set(View.VISIBLE);
    }

    private boolean isActionVisible() {
        return actionVisibility.get() == View.VISIBLE;
    }

    private boolean isDividerVisible() {
        return dividerVisibility.get() == View.VISIBLE;
    }

    public void switchActionVisibility() {
        actionVisibility.set(isActionVisible() ? View.GONE : View.VISIBLE);
        dividerVisibility.set(isDividerVisible() ? View.INVISIBLE : View.VISIBLE);
    }

    public boolean isCompleted() {
        return mTodo.isCompleted();
    }

    public boolean actionShowing() {
        return actionVisibility.get() == View.VISIBLE;
    }

    public float getOpacity() {
        return mTodo.isCompleted() ? 0.5f : 1.0f;
    }

    public String getTitle() {
        return mTodo.getTitle();
    }

    public String getDueDate() {
        return mTodo.getDueDate() == null ? NO_DUE_DATE : DateFormat.format("dd/MM/yyyy", mTodo.getDueDate()).toString();
    }

    public Priority getPriority() {
        return mTodo.getPriority();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TodoViewModel && ((TodoViewModel) obj).getTodo().equals(mTodo);
    }
}
