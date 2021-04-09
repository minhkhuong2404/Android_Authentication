package com.example.authentication.Activity.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authentication.Activity.AbstractActivity;
import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.R;
import com.example.authentication.Activity.Walkthrough.SlideActivity;

import io.paperdb.Paper;

public class NewAccount extends AbstractActivity {

    private Button signUpButton, backButton;
    private EditText emailText;
    private TextView newAccountTextView, enterEmailTextView, alreadyHaveAccountTextView, ifExistsTextView ;
    private RadioGroup switchLanguage;
    private RadioButton english, vietnamese;

    @Override
    public void UpdateLanguage(String language) {

    }

    private boolean valid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.new_account);

        Paper.init(this);
        String language = Paper.book().read("language");
        if (language == null) {
            Paper.book().write("language", "en");
        }

        newAccountTextView = findViewById(R.id.new_account);
        enterEmailTextView = findViewById(R.id.enter_email_text);
        alreadyHaveAccountTextView = findViewById(R.id.already_have_account);
        ifExistsTextView = findViewById(R.id.sign_in_if_exists);
        signUpButton = findViewById(R.id.new_account_continue_btn);
        backButton = findViewById(R.id.back_btn_new_account);
        emailText = findViewById(R.id.enter_email_field);
        english = findViewById(R.id.english_lan);
        vietnamese = findViewById(R.id.vietnamese_lan);

        switchLanguage = findViewById(R.id.toggle);

        switchLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.english_lan) {
                    Paper.book().write("language", "en");
                    english.setChecked(true);
                } else if (checkedId == R.id.vietnamese_lan) {
                    Paper.book().write("language", "vi");
                    vietnamese.setChecked(true);
                }
                putStringPref("language", Paper.book().read("language"));
                updateView((String)Paper.book().read("language"));
                Log.d("Language", Paper.book().read("language") + " ---- " + language);
            }
        });

        if (language.equals("en")) {
            Paper.book().write("language", "en");
            english.setVisibility(View.VISIBLE);
            english.setChecked(true);
        } else {
            Paper.book().write("language", "vi");
            vietnamese.setVisibility(View.VISIBLE);
            vietnamese.setChecked(true);
        }

        putStringPref("language", Paper.book().read("language"));
        updateView((String)Paper.book().read("language"));

        signUpButton.setOnClickListener(view -> signup());
        emailText.requestFocus();

        emailText.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                signUpButton.performClick();
            }
            return false;
        });

        ifExistsTextView.setOnClickListener(v -> {
            switchToLogIn();
            finish();
        });

        backButton.setOnClickListener(v -> {
            switchToWalkthrough();
            finish();
        });

    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();

        signUpButton.setText(resources.getString(R.string.continue_btn));
        newAccountTextView.setText(resources.getString(R.string.new_account));
        enterEmailTextView.setText(resources.getString(R.string.enter_your_email_address_below));
        alreadyHaveAccountTextView.setText(resources.getString(R.string.already_have_an_account));
        ifExistsTextView.setText(resources.getString(R.string.sign_in));
        emailText.setHint(resources.getString(R.string.enter_email_field_log_in));
    }

    private void switchToCreatePassword() {
        Intent switchToCreatePassword = new Intent(this, CreatePassword.class);
        switchToCreatePassword.putExtra("email", emailText.getText().toString());
        startActivity(switchToCreatePassword);
    }

    private void switchToLogIn() {
        Intent intent = new Intent(this, LogIn.class);
        intent.putExtra("categories", getIntent().getSerializableExtra("myCategories"));
        startActivity(intent);
    }

    private void switchToWalkthrough() {
        startActivity(new Intent(this, SlideActivity.class));
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
        String email = emailText.getText().toString();
        valid = checkEmail(email);
        return valid;
    }

    public boolean checkEmail(String email) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email address");
            return false;
        } else {
            emailText.setError(null);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}