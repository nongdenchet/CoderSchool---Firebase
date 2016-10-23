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

import apidez.com.firebase.R;
import apidez.com.firebase.activity.LoginActivity;
import apidez.com.firebase.adapter.TodoListAdapter;
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
        // TODO: implement this
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
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
    }

    private void removeTodo(TodoViewModel viewModel) {
        // TODO: implement this
    }

    private void completeTodo(TodoViewModel viewModel) {
        // TODO: implement this
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchTodos();
    }

    private void fetchTodos() {
        // TODO: implement this
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
        // TODO: implement this
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
