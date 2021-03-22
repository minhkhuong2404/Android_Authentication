package com.example.authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authentication.Home.Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LogIn extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.back_btn_log_in) Button backToCreateAccount;
    @BindView(R.id.enter_email_field_log_in) EditText loginEmail;
    @BindView(R.id.enter_email_password_log_in) EditText loginPassword;
    @BindView(R.id.log_in_continue_btn) Button loginButton;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        unbinder = ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        backToCreateAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                backToCreateAccount();
                finish();
            }
        });

        loginEmail.setHint("Your Email");
        loginEmail.setText("asdasdad@cm.com");
        loginPassword.setHint("Your Password");
        loginPassword.setText("asdqwe123");
        loginEmail.requestFocus();

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addCourse();
                checkLogin();
            }
        });

        loginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                    loginButton.performClick();
                }
                return false;

            }
        });

    }

    public void addCourse() {
        DataHandler dbHandler = new DataHandler(this, null, null, 1);
        dbHandler.addDataHandler(new Course("Sketch App Masterclass","$ 340", "$ 199", "3.0", R.drawable.orange_background, "Design"));
        dbHandler.addDataHandler(new Course("Figma App Materclass","$ 350", "$ 199", "5.0", R.drawable.orange_background, "Design"));
        dbHandler.addDataHandler(new Course("Business Master Class","$ 440", "$ 299", "4.8", R.drawable.orange_background, "Design"));
        dbHandler.addDataHandler(new Course("Adobe XD Masterclass","$ 140", "$ 99", "4.8", R.drawable.orange_background, "Design"));
        dbHandler.addDataHandler(new Course("Photoshop CC Masterclass","$ 540", "$ 399", "4.5", R.drawable.orange_background, "Design"));
        dbHandler.addDataHandler(new Course("Illustrator CC Masterclass","$ 640", "$ 499", "4.8", R.drawable.orange_background, "Design"));
        dbHandler.addDataHandler(new Course("Premiere Pro CC Masterclass","$ 849", "$ 599", "4.7", R.drawable.orange_background, "Design"));
        dbHandler.addDataHandler(new Course("Business Masterclass", "$ 450", "$ 299", "5.0", R.drawable.orange_background, "Business"));
        dbHandler.addDataHandler(new Course("Business Introduction", "$ 350", "$ 199", "4.6", R.drawable.orange_background, "Business"));
        dbHandler.addDataHandler(new Course("Business Management", "$ 250", "$ 99", "3.8", R.drawable.orange_background, "Business"));
    }

    private boolean checkLogin() {
        Log.d("Tag", "Login");
        final boolean[] result = new boolean[1];
        if (!validate()) {
            onLogInFail();
            return false;
        }
        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LogIn.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Log in your account");
        progressDialog.show();

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        result[0] = true;
                        onLogInSuccess();
                    }
                }, 1000);
        return result[0];
    }

    private void backToCreateAccount() {
        Intent backToCreateAccount = new Intent(this, NewAccount.class);
        startActivity(backToCreateAccount);
    }

    private void switchToHome() {
        Intent switchToHome = new Intent(this, MainActivity.class);
        switchToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        switchToHome.putExtra("username", loginEmail.getText().toString());
        startActivity(switchToHome);
    }

    public void onLogInSuccess() {
        Log.d("valid", "login_successs");
        loginButton.setEnabled(true);
        mAuth.signInWithEmailAndPassword(loginEmail.getText().toString(), loginPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LogIn.this, "Log in successfully.", Toast.LENGTH_SHORT).show();
                            switchToHome();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        setResult(RESULT_OK, null);

    }

    public void onLogInFail() {
        Log.d("valid", "login_failed");
        Toast.makeText(this, "Log in failed. Please check your account detail.", Toast.LENGTH_SHORT).show();
    }

    public boolean validate() {
        boolean valid = true;
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        if ( password.isEmpty() || password.length() < 8 ) {
            loginPassword.setError("Enter a valid password");
            valid = false;
        } else {
            loginPassword.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            loginEmail.setError("Enter a valid email address");
            valid = false;
        }
        else {
            loginEmail.setError(null);
        }

        return valid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
