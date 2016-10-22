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
import apidez.com.firebase.utils.view.AnimationUtils;
import apidez.com.firebase.viewmodel.TodoViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nongdenchet on 2/8/16.
 */
public class TodoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TodoViewModel> mTodos;

    public TodoListAdapter() {
        mTodos = new ArrayList<>();
    }

    public TodoListAdapter(List<TodoViewModel> todos) {
        mTodos = todos;
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
        public TodoViewModel viewModel;

        @BindView(R.id.pop_checkbox)
        public PopCheckBox popCheckBox;

        @BindView(R.id.todo)
        public View todoView;

        @BindView(R.id.edit_button)
        public View editButton;

        @BindView(R.id.delete_button)
        public View deleteButton;

        public ViewHolder(ItemTodoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TodoViewModel viewModel) {
            this.viewModel = viewModel;
            ButterKnife.bind(this, binding.getRoot());
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
            todoView.setOnClickListener(view -> {
                // TODO: implement
            });
            popCheckBox.setOnClickListener(view -> {
                animateCheckChange();
            });
        }

        public void animateCheckChange() {
            boolean test = !viewModel.isCompleted();
            viewModel.getTodo().setCompleted(test);
            popCheckBox.animateChecked(test);
            AnimationUtils.animateAlpha(todoView,
                    viewModel.getOpacity(), ALPHA_ANIM_DURATION);
        }
    }
}
