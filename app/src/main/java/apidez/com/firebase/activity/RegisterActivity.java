package apidez.com.firebase.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import apidez.com.firebase.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nongdenchet on 10/22/16.
 */

public class RegisterActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;

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
        mFirebaseAuth = FirebaseAuth.getInstance();
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
        if (TextUtils.isEmpty(email)) {
            showError("Email is empty");
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
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> startActivity(TodoActivity.getIntent(this)))
                .addOnFailureListener(e -> showError(e.getMessage()))
                .addOnCompleteListener(task -> mProgressDialog.hide());
    }

    private void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
