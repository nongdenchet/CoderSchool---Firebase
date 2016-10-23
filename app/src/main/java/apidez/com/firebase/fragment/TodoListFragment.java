package apidez.com.firebase.fragment;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import apidez.com.firebase.R;
import apidez.com.firebase.activity.LoginActivity;
import apidez.com.firebase.adapter.TodoListAdapter;
import apidez.com.firebase.config.FirebaseConfig;
import apidez.com.firebase.model.Todo;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nongdenchet on 2/8/16.
 */
public class TodoListFragment extends Fragment implements TodoDialogFragment.CallbackSuccess {
    private TodoListAdapter mTodoListAdapter;
    private TodoDialogFragment mTodoDialogFragment;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

    @BindView(R.id.todoList)
    RecyclerView mTodoList;

    @BindView(R.id.emptyContainer)
    View emptyContainer;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todo_list, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mTodoList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTodoListAdapter = new TodoListAdapter();
        mTodoListAdapter.setOnSizeChangeLitener(size ->
                emptyContainer.setVisibility(size > 0 ? View.GONE : View.VISIBLE));
        mTodoListAdapter.setItemClickListener(viewModel -> showTodoDialog(viewModel.getTodo()));
        mTodoList.setAdapter(mTodoListAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child(FirebaseConfig.TODOS_CHILD)
                .addValueEventListener(new ValueEventListener() {
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
            todos.add(child.getValue(Todo.class));
            mTodoListAdapter.setTodos(todos);
        }
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
