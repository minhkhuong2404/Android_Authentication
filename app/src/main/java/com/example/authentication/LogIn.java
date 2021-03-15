package com.example.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LogIn extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.back_btn_log_in) Button backToCreateAccount;
    @BindView(R.id.enter_email_field_log_in) EditText loginEmail;
    @BindView(R.id.enter_email_password_log_in) EditText loginPassword;
    @BindView(R.id.log_in_continue_btn) Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        unbinder = ButterKnife.bind(this);

        backToCreateAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                backToCreateAccount();
                finish();
            }
        });


        loginEmail.setHint("Your Email");
        loginEmail.setText("asasdasd@vn.com");
        loginPassword.setHint("Your Password");
        loginPassword.setText("asdasdasdadasdad");
        loginEmail.requestFocus();

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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
                        switchToHome();
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
        startActivity(switchToHome);
    }

    public void onLogInSuccess() {
        Log.d("valid", "login_successs");
        loginButton.setEnabled(true);
        Toast.makeText(this, "Log in successfully.", Toast.LENGTH_SHORT).show();
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
