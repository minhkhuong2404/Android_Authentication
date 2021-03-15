package com.example.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ybs.passwordstrengthmeter.PasswordStrength;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CreatePassword extends AppCompatActivity implements TextWatcher {

    Unbinder unbinder;
    @BindView(R.id.back_btn_create_password) Button backToNewAccount;
    @BindView(R.id.create_password_continue_btn) Button signUpButton;
    @BindView(R.id.enter_password_field) EditText passwordText;
    @BindView(R.id.sign_in_if_exists_create_password) TextView loginText;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.password_strength) TextView strengthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_password);
        unbinder = ButterKnife.bind(this);

        backToNewAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               backToNewAccount();
               finish();
            }
        });

        passwordText.addTextChangedListener(this);
        passwordText.setHint("Your Password");
        passwordText.requestFocus();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                switchToPhoneNumber();
                finish();
            }
        });


    }

    private void backToNewAccount() {
        Intent backToNewAccount = new Intent(this, NewAccount.class);
        startActivity(backToNewAccount);
    }

    private void switchToPhoneNumber() {
        Intent intent = new Intent(this, PhoneNumber.class);
        startActivity(intent);
    }
    private boolean signup() {
        Log.d("Tag", "Password");
        final boolean[] result = new boolean[1];
        if (!validate()) {
            onSignUpFailed();
            return false;
        }
        signUpButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(CreatePassword.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating account");
        progressDialog.show();

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        onSignupSuccess();
                        progressDialog.dismiss();
                        result[0] = true;
                        switchToPhoneNumber();
                    }
                }, 1000);
        return result[0];
    }

    public void onSignupSuccess() {
        Log.d("valid", "pw_successs");
        signUpButton.setEnabled(true);
        Toast.makeText(this, "New account created", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, null);

    }

    public void onSignUpFailed() {
        Log.d("valid", "pw_failed");
        Toast.makeText(this, "Wrong email address", Toast.LENGTH_SHORT).show();
    }

    public boolean validate() {
        boolean valid = true;
        String password = passwordText.getText().toString();

        if (password.isEmpty() || password.length() < 8) {
            passwordText.setError("Enter a valid password");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updatePasswordStrengthView(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void updatePasswordStrengthView(String password) {


        if (TextView.VISIBLE != strengthView.getVisibility())
            return;

        if (password.isEmpty()) {
            strengthView.setText("");
            progressBar.setProgress(0);
            return;
        }

        PasswordStrength str = PasswordStrength.calculateStrength(password);
        strengthView.setText(str.getText(this));
        strengthView.setTextColor(str.getColor());

        progressBar.getProgressDrawable().setColorFilter(str.getColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        if (str.getText(this).equals("Weak")) {
            progressBar.setProgress(25);
        } else if (str.getText(this).equals("Medium")) {
            progressBar.setProgress(50);
        } else if (str.getText(this).equals("Strong")) {
            progressBar.setProgress(75);
        } else {
            progressBar.setProgress(100);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
