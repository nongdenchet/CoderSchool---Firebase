package apidez.com.firebase.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import apidez.com.firebase.R;
import apidez.com.firebase.fragment.TodoListFragment;

/**
 * Created by nongdenchet on 2/8/16.
 */
public class TodoActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        drawStatusBar();
        addFragmentTodoList();
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, TodoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    private void addFragmentTodoList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, TodoListFragment.newInstance())
                .commit();
    }

    private void drawStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }
}
