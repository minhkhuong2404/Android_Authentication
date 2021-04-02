package com.example.authentication.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authentication.Authentication.CreatePassword;
import com.example.authentication.Authentication.LogIn;
import com.example.authentication.R;
import com.example.authentication.Walkthrough.SlideActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewAccount extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.new_account_continue_btn) Button signUpButton;
    @BindView(R.id.enter_email_field) EditText emailText;
    @BindView(R.id.sign_in_if_exists) TextView loginText;
    @BindView(R.id.back_btn_new_account) Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
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

        emailText.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToWalkthrough();
                finish();
            }
        });

    }

    private void switchToCreatePassword() {
        Intent switchToCreatePassword = new Intent(this, CreatePassword.class);
        switchToCreatePassword.putExtra("email", emailText.getText().toString());
        startActivity(switchToCreatePassword);
    }

    private void switchToLogIn() {
        ArrayList<String> categories = (ArrayList<String>) getIntent().getSerializableExtra("myCategories");
        Intent intent = new Intent(this, LogIn.class);
        intent.putExtra("categories", categories);
        startActivity(intent);
    }

    private void switchToWalkthrough() {
        Intent switchToWalk = new Intent(this, SlideActivity.class);
        startActivity(switchToWalk);
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
                (this, "Your account " + emailText.getText().toString(), Toast.LENGTH_SHORT).show();
//        Intent intent2 = new Intent("emailAddress");
//        intent2.putExtra("email", emailText.getText().toString());
//        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent2);

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
    }
}