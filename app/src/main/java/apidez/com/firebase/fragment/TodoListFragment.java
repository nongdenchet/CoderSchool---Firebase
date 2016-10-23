package apidez.com.firebase.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apidez.com.firebase.R;
import apidez.com.firebase.activity.LoginActivity;
import apidez.com.firebase.adapter.TodoListAdapter;
import apidez.com.firebase.config.FirebaseConfig;
import apidez.com.firebase.model.Todo;
import apidez.com.firebase.viewmodel.TodoViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nongdenchet on 2/8/16.
 */
public class TodoListFragment extends Fragment implements TodoDialogFragment.CallbackSuccess {
    private TodoListAdapter mTodoListAdapter;
    private ProgressDialog mProgressDialog;
    private TodoDialogFragment mTodoDialogFragment;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;

    @BindView(R.id.todoList)
    RecyclerView mTodoList;

    @BindView(R.id.container)
    View container;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static TodoListFragment newInstance() {
        Bundle args = new Bundle();
        TodoListFragment fragment = new TodoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            startActivity(LoginActivity.getIntent(getActivity()));
        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todo_list, container, false);
        ButterKnife.bind(this, rootView);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Processing...");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mTodoList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTodoListAdapter = new TodoListAdapter(new TodoListAdapter.Listener() {
            @Override
            public void onItemClick(TodoViewModel viewModel) {
                showTodoDialog(viewModel.getTodo());
            }

            @Override
            public void onItemComplete(TodoViewModel viewModel) {
                completeTodo(viewModel);
            }

            @Override
            public void onItemRemove(TodoViewModel viewModel) {
                removeTodo(viewModel);
            }
        });
        mTodoList.setAdapter(mTodoListAdapter);
    }

    private void removeTodo(TodoViewModel viewModel) {
        mDatabaseReference.child(FirebaseConfig.TODOS_CHILD)
                .child(viewModel.getTodo().getId())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        mTodoListAdapter.removeTodo(viewModel);
                    }
                });
    }

    private void completeTodo(TodoViewModel viewModel) {
        Todo todo = viewModel.getTodo();
        todo.setCompleted(!todo.isCompleted());
        Map<String, Object> updates = new HashMap<>();
        updates.put(todo.getId(), todo);
        mDatabaseReference.child(FirebaseConfig.TODOS_CHILD)
                .child(viewModel.getTodo().getId())
                .updateChildren(updates, (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        mTodoListAdapter.updateTodo(todo);
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        Query query = mDatabaseReference.child(FirebaseConfig.TODOS_CHILD)
                .orderByChild("uid")
                .startAt(mFirebaseUser.getUid())
                .endAt(mFirebaseUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                handleResponse(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void handleResponse(DataSnapshot dataSnapshot) {
        List<Todo> todos = new ArrayList<>();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Todo todo = child.getValue(Todo.class);
            todo.setId(child.getKey());
            todos.add(todo);
            mTodoListAdapter.setTodos(todos);
        }
        mProgressDialog.hide();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_todo_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showTodoDialog();
                return true;
            case R.id.action_logout:
                handleLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleLogout() {
        mFirebaseAuth.signOut();
        startActivity(LoginActivity.getIntent(getActivity()));
    }

    private void showTodoDialog() {
        mTodoDialogFragment = TodoDialogFragment.newInstance();
        setCallbackAndShowDialog();
    }

    private void showTodoDialog(Todo todo) {
        mTodoDialogFragment = TodoDialogFragment.newInstance(todo);
        setCallbackAndShowDialog();
    }

    private void setCallbackAndShowDialog() {
        if (mTodoDialogFragment != null) {
            mTodoDialogFragment.setCallbackSuccess(this);
            mTodoDialogFragment.show(getFragmentManager(), TodoDialogFragment.TAG);
        }
    }

    @Override
    public void onCreateSuccess(Todo todo) {
        mTodoListAdapter.addTodo(todo);
        mTodoList.smoothScrollToPosition(0);
    }

    @Override
    public void onUpdateSuccess(Todo todo) {
        mTodoListAdapter.updateTodo(todo);
    }
}
