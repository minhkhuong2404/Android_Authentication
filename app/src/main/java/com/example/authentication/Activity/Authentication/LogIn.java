package com.example.authentication.Activity.Authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authentication.Model.Handler.NotificationHandler;
import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.Activity.AbstractActivity;
import com.example.authentication.R;
import com.example.authentication.Activity.Splash;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;

public class LogIn extends AbstractActivity {

    private TextView logIn, enterLogInDetail;
    private EditText loginEmail, loginPassword;
    private Button loginButton, backToCreateAccount;
    private FirebaseAuth mAuth;
    private boolean valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.log_in);

        logIn = findViewById(R.id.log_in);
        enterLogInDetail = findViewById(R.id.enter_login_details);
        loginEmail = findViewById(R.id.enter_email_field_log_in);
        loginPassword = findViewById(R.id.enter_email_password_log_in);
        backToCreateAccount = findViewById(R.id.back_btn_log_in);
        loginButton = findViewById(R.id.log_in_continue_btn);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mAuth = FirebaseAuth.getInstance();
        backToCreateAccount.setOnClickListener(v -> {
            backToCreateAccount();
            finish();
        });

        loginEmail.setHint("Your Email");
        loginEmail.setText("asdasdad@cm.com");
        loginPassword.setHint("Your Password");
        loginPassword.setText("asdqwe123");
        loginEmail.requestFocus();

        loginButton.setOnClickListener(v -> {
            try {
                addNotification();
                checkLogin();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        loginPassword.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                loginButton.performClick();
            }
            return false;
        });
    }

    @Override
    public void UpdateLanguage(String language) {
        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();

        logIn.setText(resources.getString(R.string.log_in));
        enterLogInDetail.setText(resources.getString(R.string.enter_login_details));
        loginButton.setText(resources.getString(R.string.continue_btn));
        loginEmail.setHint(resources.getString(R.string.enter_email_field_log_in));
    }

    public void addNotification() throws ParseException {
        NotificationHandler notifHandler = new NotificationHandler(this, null,null,1);
//        notifHandler.deleteAllNotificationHandler();
    }

    public boolean checkLogin() {
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
                () -> {
                    progressDialog.dismiss();
                    result[0] = true;
                    onLogInSuccess();
                }, 0);
        return result[0];
    }

    private void backToCreateAccount() {
        startActivity(new Intent(this, NewAccount.class));
    }

    private void switchToSplash() {
        Intent switchToHome = new Intent(this, Splash.class);
        switchToHome.putExtra("user", loginEmail.getText().toString());
        startActivity(switchToHome);
    }

    public void onLogInSuccess() {
        Log.d("valid", "login_successs");
        loginButton.setEnabled(true);
        mAuth.signInWithEmailAndPassword(loginEmail.getText().toString(), loginPassword.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Login", "signInWithEmail:success");
//                        Toast.makeText(LogIn.this, "Log in successfully.", Toast.LENGTH_SHORT).show();
                        switchToSplash();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Login", "signInWithEmail:failure", task.getException());
//                        Toast.makeText(LogIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
        setResult(RESULT_OK, null);
    }

    public void onLogInFail() {
        Log.d("valid", "login_failed");
        Toast.makeText(this, "Log in failed. Please check your account detail.", Toast.LENGTH_SHORT).show();
    }

    public boolean validate() {

        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        valid = checkPassword(password);
        valid = checkEmail(email);
        return valid;
    }

    public boolean checkEmail(String email) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            loginEmail.setError("Enter a valid email address");
            return false;
        }
        else {
            loginEmail.setError(null);
        }
        return true;
    }

    public boolean checkPassword(String password) {
        if ( password.isEmpty() || password.length() < 8 ) {
            loginPassword.setError("Enter a valid password");
            return false;
        } else {
            loginPassword.setError(null);
        }
        return true;
    }
}
