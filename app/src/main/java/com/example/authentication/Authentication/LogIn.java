package com.example.authentication.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.authentication.Home.Course;
import com.example.authentication.Handler.CourseHandler;
import com.example.authentication.Handler.NotificationHandler;
import com.example.authentication.MainActivity;
import com.example.authentication.R;
import com.example.authentication.Splash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LogIn extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.back_btn_log_in) Button backToCreateAccount;
    @BindView(R.id.enter_email_field_log_in) EditText loginEmail;
    @BindView(R.id.enter_email_password_log_in) EditText loginPassword;
    @BindView(R.id.log_in_continue_btn) Button loginButton;
    public static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private FirebaseAuth mAuth;
    private String path = "https://6062ebee0133350017fd227f.mockapi.io/Courses";
    private URL url;
    private StringBuffer response;
    private String responseText;
    private SharedPreferences sharedPreferences ;
    private ArrayList<String> category = new ArrayList<>();

    private Random random = new Random();
    private String category1 ;
    private String category2 ;
    private String study;
    private String collection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.log_in);
        unbinder = ButterKnife.bind(this);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

//        String json = sharedPreferences.getString("GSON", "");
//        String[] category = new String[10];
//        Gson gson = new Gson();
//
//        Type type = new TypeToken<ArrayList<String>>() {}.getType();
//        passCategory = gson.fromJson(json, type);
//
//        if (passCategory != null ) {
//            category = new String[passCategory.size()];
//            passCategory.toArray(category);
//
//        }

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

    public void addCourse() {
        CourseHandler dbHandler = new CourseHandler(this, null, null, 1);
        dbHandler.deleteAllCourseHandler();
    }

    public void addNotification() throws ParseException {
        NotificationHandler notifHandler = new NotificationHandler(this, null,null,1);
//        notifHandler.deleteAllNotificationHandler();
//        List<NotificationItem> noti = notifHandler.loadNotificationHandler();
//        for (int i = 0; i < noti.size();i++) {
//            notifHandler.updateNotificationHandler(noti.get(i).getNotificationInformation(), noti.get(i).getNotificationIcon());
//        }
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
        Intent backToCreateAccount = new Intent(this, NewAccount.class);
        startActivity(backToCreateAccount);
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
                        FirebaseUser user = mAuth.getCurrentUser();
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
    }
}
