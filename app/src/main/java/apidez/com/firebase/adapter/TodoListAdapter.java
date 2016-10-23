package apidez.com.firebase.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import apidez.com.firebase.R;
import apidez.com.firebase.custom.PopCheckBox;
import apidez.com.firebase.databinding.ItemTodoBinding;
import apidez.com.firebase.model.Todo;
import apidez.com.firebase.utils.view.AnimationUtils;
import apidez.com.firebase.viewmodel.TodoViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nongdenchet on 2/8/16.
 */
public class TodoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TodoViewModel> mTodos;
    private OnItemClickListener mItemClickListener;
    private OnSizeChangeListener mOnSizeChangeLitener;

    public interface OnItemClickListener {
        void onItemClick(TodoViewModel viewModel);
    }

    public interface OnSizeChangeListener {
        void onSizeChange(int size);
    }

    public TodoListAdapter() {
        mTodos = new ArrayList<>();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setOnSizeChangeLitener(OnSizeChangeListener onSizeChangeLitener) {
        mOnSizeChangeLitener = onSizeChangeLitener;
    }

    public void setTodos(List<Todo> todos) {
        mTodos.clear();
        for (Todo todo : todos) {
            mTodos.add(new TodoViewModel(todo));
        }
        triggerSizeChange();
        notifyDataSetChanged();
    }

    public void addTodo(Todo todo) {
        mTodos.add(0, new TodoViewModel(todo));
        triggerSizeChange();
        notifyItemInserted(0);
    }

    public void updateTodo(Todo todo) {
        TodoViewModel viewModel = findTodo(todo.getId());
        if (viewModel != null) {
            viewModel.setTodo(todo);
            int position = mTodos.indexOf(viewModel);
            mTodos.set(position, viewModel);
            notifyItemChanged(position);
        }
    }

    private void removeTodo(TodoViewModel viewModel) {
        int position = mTodos.indexOf(viewModel);
        mTodos.remove(viewModel);
        resetState();
        triggerSizeChange();
        notifyItemRemoved(position);
    }

    private void resetState() {
        for (TodoViewModel todoViewModel : mTodos) {
            todoViewModel.resetState();
        }
    }

    private void triggerSizeChange() {
        if (mOnSizeChangeLitener != null) {
            mOnSizeChangeLitener.onSizeChange(mTodos.size());
        }
    }

    private TodoViewModel findTodo(int id) {
        for (TodoViewModel viewModel : mTodos) {
            if (viewModel.getTodo().getId() == id) {
                return viewModel;
            }
        }
        return null;
    }

    private void editTodo(TodoViewModel viewModel) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(viewModel);
        }
    }

    private void clickTodo(TodoViewModel viewModel) {
        viewModel.switchActionVisibility();
        for (TodoViewModel todoViewModel : mTodos) {
            if (todoViewModel != viewModel) {
                todoViewModel.switchEnableWhenNotChoose();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mTodos.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTodoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_todo, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bind(mTodos.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final int ALPHA_ANIM_DURATION = 150;
        private ItemTodoBinding binding;

        @BindView(R.id.pop_checkbox)
        public PopCheckBox popCheckBox;

        @BindView(R.id.todo)
        public View todoView;

        @BindView(R.id.edit_button)
        public View editButton;

        @BindView(R.id.delete_button)
        public View deleteButton;

        @BindView(R.id.disable_layer)
        public View disableLayer;

        public ViewHolder(ItemTodoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            ButterKnife.bind(this, binding.getRoot());
        }

        public void bind(TodoViewModel viewModel) {
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
            todoView.setOnClickListener(view -> clickTodo(viewModel));
            editButton.setOnClickListener(view -> editTodo(viewModel));
            deleteButton.setOnClickListener(view -> removeTodo(viewModel));
            popCheckBox.setOnClickListener(view -> animateCheckChange(viewModel));
            disableLayer.setOnClickListener(view -> resetState());
        }

        public void animateCheckChange(TodoViewModel viewModel) {
            boolean test = !viewModel.isCompleted();
            viewModel.getTodo().setCompleted(test);
            popCheckBox.animateChecked(test);
            AnimationUtils.animateAlpha(todoView,
                    viewModel.getOpacity(), ALPHA_ANIM_DURATION);
        }
    }
}
