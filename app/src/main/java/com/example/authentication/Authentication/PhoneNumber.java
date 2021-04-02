package com.example.authentication.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.authentication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PhoneNumber extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.back_btn_phone_number) Button backToNewAccount;
    @BindView(R.id.phone_number_continue_btn) Button addPhoneNumber;
    @BindView(R.id.enter_phone_number_field) EditText phoneNumber;
    private String lastChar = "";
    public String phone = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.phone_number);

        unbinder = ButterKnife.bind(this);

        backToNewAccount.setOnClickListener(v -> {
            backToNewAccount();
            finish();
        });

        phoneNumber.setHint("Mobile Number");
        phoneNumber.requestFocus();

        addPhoneNumber.setOnClickListener(view -> {
            if (TextUtils.isEmpty(phoneNumber.getText().toString()) && phoneNumber.getText().toString().length() != 12) {
                // when mobile number text field is empty
                // displaying a toast message.
                Toast.makeText(PhoneNumber.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            } else {
                // if the text field is not empty we are calling our
                // send OTP method for getting OTP from Firebase.
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
//                Log.d("LENGTH",""+digits);
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
        Intent backToNewAccount = new Intent(this, NewAccount.class);
        startActivity(backToNewAccount);
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
                    new Runnable() {
                        @Override
                        public void run() {
                            onSignupSuccess();
                            progressDialog.dismiss();
                            result[0] = true;
                            switchToVerification();
                        }
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
        boolean valid = true;
        String phone2 = phoneNumber.getText().toString();

        String normalPhone = phone2.replace("-", "");

        if (phone2 != null && isNumeric(normalPhone)) {
            if (phone2.length() != 12 && phone2.charAt(3) != '-' && phone2.charAt(7) != '-') {
                phoneNumber.setError("Enter a valid phone number");
                valid = false;
            } else {
                phoneNumber.setError(null);
            }
        } else {
            phoneNumber.setError("Enter a valid phone number");
            valid = false;
        }

        return valid;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
