package apidez.com.firebase.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import apidez.com.firebase.R;
import apidez.com.firebase.custom.DueDatePicker;
import apidez.com.firebase.custom.PriorityPicker;
import apidez.com.firebase.model.Todo;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nongdenchet on 2/11/16.
 */
public class TodoDialogFragment extends DialogFragment implements DueDatePicker.ListenerPickDate {
    public static final String TAG = TodoDialogFragment.class.getSimpleName();
    private static final String TODO = "todo";
    private boolean isRestore = false;
    private CallbackSuccess mCallbackSuccess;

    @BindView(R.id.discard)
    TextView mDiscardButton;

    @BindView(R.id.save)
    TextView mSaveButton;

    @BindView(R.id.title_edit_text)
    EditText mTitleEditText;

    @BindView(R.id.priority_picker)
    PriorityPicker mPriorityPicker;

    @BindView(R.id.due_date_picker)
    DueDatePicker mDueDatePicker;

    public interface CallbackSuccess {
        void onCreateSuccess(Todo todo);
        void onUpdateSuccess(Todo todo);
    }

    public void setCallbackSuccess(CallbackSuccess callbackSuccess) {
        this.mCallbackSuccess = callbackSuccess;
    }

    public static TodoDialogFragment newInstance() {
        Bundle args = new Bundle();
        TodoDialogFragment fragment = new TodoDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static TodoDialogFragment newInstance(Todo todo) {
        Bundle args = new Bundle();
        args.putSerializable(TODO, todo);
        TodoDialogFragment fragment = new TodoDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_todo, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDueDatePicker.setListenerPickDate(this);
        mDiscardButton.setOnClickListener(v -> dismiss());
        restoreTodo();
    }

    private void restoreTodo() {
        Todo todo = (Todo) getArguments().getSerializable(TODO);
        if (todo != null) {
            isRestore = true;
            restoreView(todo);
        }
    }

    private void restoreView(Todo todo) {
        mTitleEditText.setText(todo.getTitle());
        mPriorityPicker.setPriority(todo.getPriority());
        mDueDatePicker.setDueDate(todo.getDueDate());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void sendCallbackSuccess(Todo todo) {
        if (isRestore) {
            mCallbackSuccess.onUpdateSuccess(todo);
        } else {
            mCallbackSuccess.onCreateSuccess(todo);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void pickDate(DueDatePicker.CallbackPickDate callbackPickDate) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> callbackPickDate.onDatePicked(year, monthOfYear, dayOfMonth),
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
    }
}
