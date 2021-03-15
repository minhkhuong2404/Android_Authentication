package com.example.authentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Verification extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.back_btn_verification) Button backToPhoneNumber;
    @BindView(R.id.resend_verification) TextView resendCodeText;
    @BindView(R.id.verification_continue_btn) Button sendCodeButton;
    @BindView(R.id.code_1) EditText code1;
    @BindView(R.id.code_2) EditText code2;
    @BindView(R.id.code_3) EditText code3;
    @BindView(R.id.code_4) EditText code4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification);
        unbinder = ButterKnife.bind(this);

        backToPhoneNumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                backToPhoneNumber();
                finish();
            }
        });

        resendCodeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendCodeText();
            }
        });

        sendCodeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switchToLogIn();
                finish();
            }
        });

        code4.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                    sendCodeButton.performClick();
                }
                return false;

            }
        });

        code1.requestFocus();

        code1.addTextChangedListener(new GenericText(code1));
        code2.addTextChangedListener(new GenericText(code2));
        code3.addTextChangedListener(new GenericText(code3));
        code4.addTextChangedListener(new GenericText(code4));

    }

    public class GenericText implements TextWatcher {
        private View view;

        private GenericText(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void afterTextChanged(Editable s) {
            String otp = s.toString();
            switch (view.getId()) {

                case R.id.code_1:
                    if (otp.length() == 1)
                        code2.requestFocus();
                    break;
                case R.id.code_2:
                    if (otp.length() == 1)
                        code3.requestFocus();
                    else if (otp.length() == 0)
                        code1.requestFocus();
                    break;
                case R.id.code_3:
                    if (otp.length() == 1)
                        code4.requestFocus();
                    else if (otp.length() == 0)
                        code2.requestFocus();
                    break;
                case R.id.code_4:
                    if (otp.length() == 0)
                        code3.requestFocus();
                    else if (otp.length() == 1)
                        new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    switchToLogIn();
                                }
                            }, 1500);
                    break;
            }
        }

    }
    private void switchToLogIn() {
        Intent switchToLogIn = new Intent(this, LogIn.class);
        startActivity(switchToLogIn);
    }

    private void resendCodeText() {

    }

    private void backToPhoneNumber() {
        Intent backToPhoneNumber = new Intent(this, PhoneNumber.class);
        startActivity(backToPhoneNumber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
