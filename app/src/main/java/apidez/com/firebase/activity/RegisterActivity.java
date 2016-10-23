package apidez.com.firebase.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import apidez.com.firebase.R;
import apidez.com.firebase.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nongdenchet on 10/22/16.
 */

public class RegisterActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    @BindView(R.id.edtEmail)
    TextView edtEmail;

    @BindView(R.id.edtPassword)
    TextView edtPassword;

    @BindView(R.id.edtConfirmation)
    TextView edtConfirmation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setUpFirebase();
        setUpViews();
    }

    private void setUpViews() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Processing...");
    }

    private void setUpFirebase() {
        // TODO: implement this
    }

    @OnClick(R.id.btnLogin)
    public void onLoginClick() {
        startActivity(LoginActivity.getIntent(this));
    }

    @OnClick(R.id.btnRegister)
    public void onRegisterClick() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmation = edtConfirmation.getText().toString().trim();
        if (!StringUtils.isValidEmailAddress(email)) {
            showError("Invalid email");
        } else if (password.length() < 8) {
            showError("Password is too short");
        } else if (!TextUtils.equals(password, confirmation)) {
            showError("Confirmation is not match");
        } else {
            registerWithFirebase(email, password);
        }
    }

    private void registerWithFirebase(String email, String password) {
        mProgressDialog.show();
        // TODO: implement this
    }

    private void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    private void handleRegisterSuccess() {
        startActivity(TodoActivity.getIntent(this));
    }
}
