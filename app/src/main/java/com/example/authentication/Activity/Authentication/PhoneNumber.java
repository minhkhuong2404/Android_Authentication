package com.example.authentication.Activity.Authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class PhoneNumber extends AbstractActivity {
    private String lastChar = "";
    private Button backToNewAccount, addPhoneNumber;
    private EditText phoneNumber;
    private TextView phoneNumberText, enterPhoneNumberText;
    public String phone = "";

    @Override
    public void UpdateLanguage(String language) {
        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();

        addPhoneNumber.setText(resources.getString(R.string.continue_btn));
        phoneNumber.setHint(resources.getString(R.string.enter_phone_number_field));
        phoneNumberText.setText(resources.getString(R.string.phone_number));
        enterPhoneNumberText.setText(resources.getString(R.string.enter_phone_number));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.phone_number);
        backToNewAccount = findViewById(R.id.back_btn_phone_number);
        addPhoneNumber = findViewById(R.id.phone_number_continue_btn);
        phoneNumber = findViewById(R.id.enter_phone_number_field);
        phoneNumberText = findViewById(R.id.phone_number);
        enterPhoneNumberText = findViewById(R.id.enter_phone_number);

        backToNewAccount.setOnClickListener(v -> {
            backToNewAccount();
            finish();
        });

        phoneNumber.requestFocus();

        addPhoneNumber.setOnClickListener(view -> {
            if (TextUtils.isEmpty(phoneNumber.getText().toString()) && phoneNumber.getText().toString().length() != 12) {
                Toast.makeText(PhoneNumber.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            } else {
                phone = "+1" + phoneNumber.getText().toString().replace("-","");
            }
            signup();
        });

        phoneNumber.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                addPhoneNumber.performClick();
            }
            return false;

        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = phoneNumber.getText().toString().length();
                if (digits > 1)
                    lastChar = phoneNumber.getText().toString().substring(digits-1);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int digits = phoneNumber.getText().toString().length();
                if (!lastChar.equals("-")) {
                    if (digits == 3 || digits == 7) {
                        phoneNumber.append("-");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
    private void backToNewAccount() {
        startActivity(new Intent(this, NewAccount.class));
    }

    private void switchToVerification() {
        Intent intent = new Intent(this, Verification.class);
        intent.putExtra("phone", phone);
        startActivity(intent);
    }

    private boolean signup() {
        Log.d("Tag", "Phone");
        final boolean[] result = new boolean[1];
        if (!validate()) {
            onSignUpFailed();
            return false;
        }  else {
            addPhoneNumber.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(PhoneNumber.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Sending code");
            progressDialog.show();

            new Handler().postDelayed(
                    () -> {
                        onSignupSuccess();
                        progressDialog.dismiss();
                        result[0] = true;
                        switchToVerification();
                    }, 1000);
            return result[0];
        }
    }

    public void onSignupSuccess() {
        Log.d("valid", "phone_successs");
        addPhoneNumber.setEnabled(true);
        Toast.makeText(this, "New phone added", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, null);

    }

    public void onSignUpFailed() {
        Log.d("valid", "phone_failed");
        Toast.makeText(this, "Wrong phone number", Toast.LENGTH_SHORT).show();
    }

    public boolean validate() {
        boolean valid;
        String phone2 = phoneNumber.getText().toString();
        valid = checkPhoneNumber(phone2);
        return valid;
    }

    public boolean checkPhoneNumber(String phone) {
        String normalPhone = phone.replace("-", "");
        if (phone != null && isNumeric(normalPhone)) {
            if (phone.length() != 12 && phone.charAt(3) != '-' && phone.charAt(7) != '-') {
                phoneNumber.setError("Enter a valid phone number");
                return false;
            } else {
                phoneNumber.setError(null);
            }
        } else {
            phoneNumber.setError("Enter a valid phone number");
            return false;
        }
        return true;
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
