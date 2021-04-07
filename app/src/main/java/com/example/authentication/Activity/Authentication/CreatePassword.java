package com.example.authentication.Activity.Authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.Activity.AbstractActivity;
import com.example.authentication.R;
import com.google.firebase.auth.FirebaseAuth;

public class CreatePassword extends AbstractActivity {

    private static String email = "";
    private TextView createPasswordTextView, enterPasswordTextView, ifExistsTextView, loginText;
    private Button backToNewAccount, signUpButton;
    private EditText passwordText;

    private FirebaseAuth mAuth;

    @Override
    public void UpdateLanguage(String language) {
        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();

        createPasswordTextView.setText(resources.getString(R.string.create_password));
        enterPasswordTextView.setText(resources.getString(R.string.enter_create_password));
        ifExistsTextView.setText(resources.getString(R.string.already_have_account_create_password));
        loginText.setText(resources.getString(R.string.sign_in_if_exists_create_password));
        signUpButton.setText(resources.getString(R.string.continue_btn));
        passwordText.setHint(resources.getString(R.string.enter_password_field));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.create_password);

        createPasswordTextView = findViewById(R.id.create_password);
        enterPasswordTextView = findViewById(R.id.enter_create_password);
        ifExistsTextView = findViewById(R.id.already_have_account_create_password);
        backToNewAccount = findViewById(R.id.back_btn_create_password);
        signUpButton = findViewById(R.id.create_password_continue_btn);
        passwordText = findViewById(R.id.enter_password_field);
        loginText = findViewById(R.id.sign_in_if_exists_create_password);

        mAuth = FirebaseAuth.getInstance();
        backToNewAccount.setOnClickListener(v -> {
           backToNewAccount();
           finish();
        });

        signUpButton.setOnClickListener(view -> signup());

        passwordText.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                signUpButton.performClick();
            }
            return false;

        });

        loginText.setOnClickListener(v -> {
            switchToPhoneNumber();
            finish();
        });
    }

    private void backToNewAccount() {
        startActivity(new Intent(this, NewAccount.class));
    }

    private void switchToPhoneNumber() {
        startActivity(new Intent(this, PhoneNumber.class));
    }

    private void switchToLogIn() {
        startActivity(new Intent(this, LogIn.class));
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
        progressDialog.setMessage(getResources().getString(R.string.creating_dialog));
        progressDialog.show();

        new Handler().postDelayed(
                () -> {
                    onSignupSuccess();
                    progressDialog.dismiss();
                    result[0] = true;
                    switchToPhoneNumber();
                }, 1000);
        return result[0];
    }

    public void onSignupSuccess() {
        Log.d("valid", "pw_success");
        signUpButton.setEnabled(true);
        email = getIntent().getStringExtra("email");
        Log.d("Email : Password", email + " : " + passwordText.getText().toString());

        mAuth.createUserWithEmailAndPassword(email, passwordText.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Account", "createUserWithEmail:success");
                        Toast.makeText(CreatePassword.this, "New account created", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Account", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(CreatePassword.this, "No Account Created. Please login",
                                Toast.LENGTH_SHORT).show();
                        switchToLogIn();
                    }
                });

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
    protected void onDestroy() {
        super.onDestroy();
    }
}
