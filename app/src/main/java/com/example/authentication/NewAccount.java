package com.example.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NewAccount extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.new_account_continue_btn) Button signUpButton;
    @BindView(R.id.enter_email_field) EditText emailText;
    @BindView(R.id.sign_in_if_exists) TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account);

        unbinder = ButterKnife.bind(this);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
        emailText.setHint("Your Email");
        emailText.requestFocus();

        emailText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                    signUpButton.performClick();
                }
                return false;

            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLogIn();
                finish();
            }
        });

    }

    private void switchToCreatePassword() {
        Intent switchToCreatePassword = new Intent(this, CreatePassword.class);
        startActivity(switchToCreatePassword);
    }

    private void switchToLogIn() {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    private boolean signup() {
        Log.d("Tag", "Sign up");
        final boolean[] result = new boolean[1];
        if (!validate()) {
            onSignUpFailed();
            return false;
        }
        signUpButton.setEnabled(false);

        onSignupSuccess();
        Toast.makeText
                (this, "Your account is available to use", Toast.LENGTH_SHORT).show();
        switchToCreatePassword();

        return result[0];
    }

    public void onSignupSuccess() {
        Log.d("valid", "successs");
        signUpButton.setEnabled(true);
        setResult(RESULT_OK, null);
    }

    public void onSignUpFailed() {
        Log.d("valid", "failed");
        Toast.makeText(this, "Wrong email address", Toast.LENGTH_SHORT).show();
    }

    public boolean validate() {
        boolean valid = true;
        String email = emailText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        return valid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}