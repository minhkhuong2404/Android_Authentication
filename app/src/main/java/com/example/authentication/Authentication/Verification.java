package com.example.authentication.Authentication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.authentication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Verification extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String verificationId;
    public String otpFull = "";
    private ArrayList<String> letter = new ArrayList<>();
    private StringBuilder sb = new StringBuilder();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private ProgressDialog loadingBar;
    private int numberOfOtpSent = 0;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    EditText code1 ;
    EditText code2 ;
    EditText code3 ;
    EditText code4 ;
    EditText code5 ;
    EditText code6 ;

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("notify");

//            Log.d("OTP", otpFull);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT ).show();
            otpFull = message;
            System.out.println("Your otp: " + otpFull);
//            EditText code1 = findViewById(R.id.code_1);
//            EditText code2 = findViewById(R.id.code_2);
//            EditText code3 = findViewById(R.id.code_3);
//            EditText code4 = findViewById(R.id.code_4);
//            EditText code5 = findViewById(R.id.code_5);
//            EditText code6 = findViewById(R.id.code_6);

            if (!otpFull.equals("") && numberOfOtpSent == 0) {
                code1.setText(String.valueOf(otpFull.charAt(0)));
                code2.setText(String.valueOf(otpFull.charAt(1)));
                code3.setText(String.valueOf(otpFull.charAt(2)));
                code4.setText(String.valueOf(otpFull.charAt(3)));
                code5.setText(String.valueOf(otpFull.charAt(4)));
                code6.setText(String.valueOf(otpFull.charAt(5)));
                numberOfOtpSent += 1;
            }

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.verification);
//        unbinder = ButterKnife.bind(this);
        loadingBar = new ProgressDialog(this);
        System.out.println("Your otp is created : " + otpFull);

        Button backToPhoneNumber = findViewById(R.id.back_btn_verification);
        TextView resendCodeText = findViewById(R.id.resend_verification);
        Button sendCodeButton = findViewById(R.id.verification_continue_btn);

        mAuth = FirebaseAuth.getInstance();

        backToPhoneNumber.setOnClickListener(v -> {
            backToPhoneNumber();
            finish();
        });
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                mMessageReceiver, new IntentFilter("Notification"));

        code1 = findViewById(R.id.code_1);
        code2 = findViewById(R.id.code_2);
        code3 = findViewById(R.id.code_3);
        code4 = findViewById(R.id.code_4);
        code5 = findViewById(R.id.code_5);
        code6 = findViewById(R.id.code_6);

        resendCodeText.setOnClickListener(v -> {
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                    mMessageReceiver, new IntentFilter("Notification"));

            if (!otpFull.equals("") && numberOfOtpSent == 0) {
                code1.setText(String.valueOf(otpFull.charAt(0)));
                code2.setText(String.valueOf(otpFull.charAt(1)));
                code3.setText(String.valueOf(otpFull.charAt(2)));
                code4.setText(String.valueOf(otpFull.charAt(3)));
                code5.setText(String.valueOf(otpFull.charAt(4)));
                code6.setText(String.valueOf(otpFull.charAt(5)));
                numberOfOtpSent += 1;
            }
        });

        sendCodeButton.setOnClickListener(v -> {

            if (TextUtils.isEmpty(otpFull)) {
                // if the OTP text field is empty display
                // a message to user to enter OTP
                Toast.makeText(Verification.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
//                loadingBar.setTitle("Phone Verification");
//                loadingBar.setMessage("Please wait, while we are authenticating using your phone...");
//                loadingBar.setCanceledOnTouchOutside(false);
//                loadingBar.show();
//                Intent intent = getIntent();
//                String phoneNum = intent.getStringExtra("phone");
                // +16505552345
//                PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNum, 60, TimeUnit.SECONDS, Verification.this, callbacks);

            } else {
                // if OTP field is not empty calling
                // method to verify the OTP.
                Log.d("Check OTP", String.join("", letter) + " --- " + otpFull);
                if (String.join("", letter) != null && (otpFull != null)) {
                    if (otpFull.equals(String.join("", letter))) {
                        Toast.makeText(getApplicationContext(), "Successfully verify OTP", Toast.LENGTH_SHORT).show();
                        switchToLogIn();
                    } else {
                        Toast.makeText(getApplicationContext(), "Check your OTP code again", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d("FCM", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
//                        String msg = getString(R.string.app_name, token);
                    Log.d("FCM", token);
//                        Toast.makeText(Verification.this, token, Toast.LENGTH_SHORT).show();
                });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                Toast.makeText(Verification.this, "Invalid Phone Number, Please enter correct phone number with your country code...", Toast.LENGTH_LONG).show();
                loadingBar.dismiss();

            }

            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token)
            {
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;


                Toast.makeText(Verification.this, "Code has been sent, please check and verify...", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        };

        code6.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                sendCodeButton.performClick();
            }
            return false;

        });

        code1.requestFocus();

        code1.addTextChangedListener(new GenericText(code1));
        code2.addTextChangedListener(new GenericText(code2));
        code3.addTextChangedListener(new GenericText(code3));
        code4.addTextChangedListener(new GenericText(code4));
        code5.addTextChangedListener(new GenericText(code5));
        code6.addTextChangedListener(new GenericText(code6));
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            loadingBar.dismiss();
                            Toast.makeText(Verification.this, "Congratulations, you're logged in Successfully.", Toast.LENGTH_SHORT).show();
                            switchToLogIn();
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(Verification.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
    }

    public class GenericText implements TextWatcher {
        private View view;
        EditText code1 = findViewById(R.id.code_1);
        EditText code2 = findViewById(R.id.code_2);
        EditText code3 = findViewById(R.id.code_3);
        EditText code4 = findViewById(R.id.code_4);
        EditText code5 = findViewById(R.id.code_5);
        EditText code6 = findViewById(R.id.code_6);

        private GenericText(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint("NonConstantResourceId")
        @Override
        public void afterTextChanged(Editable s) {
            String otp = s.toString();
            switch (view.getId()) {

                case R.id.code_1:
                    if (otp.length() == 1) {
                        code2.requestFocus();
                        letter.add(code1.getText().toString());
                    } else if (otp.length() == 0) {
                        letter.remove(0);
                    }
                    break;
                case R.id.code_2:
                    if (otp.length() == 1) {
                        code3.requestFocus();
                        letter.add(code2.getText().toString());
                    }
                    else if (otp.length() == 0) {
                        code1.requestFocus();
                        letter.remove(1);
                    }
                    break;
                case R.id.code_3:
                    if (otp.length() == 1) {
                        code4.requestFocus();
                        letter.add(code3.getText().toString());
                    }
                    else if (otp.length() == 0) {
                        code2.requestFocus();
                        letter.remove(2);
                    }
                    break;
                case R.id.code_4:
                    if (otp.length() == 1) {
                        code5.requestFocus();
                        letter.add(code4.getText().toString());
                    }
                    else if (otp.length() == 0) {
                        code3.requestFocus();
                        letter.remove(3);
                    }
                    break;
                case R.id.code_5:
                    if (otp.length() == 1) {
                        code6.requestFocus();
                        letter.add(code5.getText().toString());
                    }
                    else if (otp.length() == 0) {
                        code4.requestFocus();
                        letter.remove(4);
                    }
                    break;
                case R.id.code_6:
                    if (otp.length() == 0) {
                        code5.requestFocus();
                        letter.remove(5);
                    }
                    else if (otp.length() == 1)
                        letter.add(code6.getText().toString());
                    break;
            }

        }
    }


    private void switchToLogIn() {
        Intent switchToLogIn = new Intent(this, LogIn.class);
        startActivity(switchToLogIn);
    }

    private void backToPhoneNumber() {
        Intent backToPhoneNumber = new Intent(this, PhoneNumber.class);
        startActivity(backToPhoneNumber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
