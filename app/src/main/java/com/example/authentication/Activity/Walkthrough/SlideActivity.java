package com.example.authentication.Activity.Walkthrough;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.authentication.Activity.Authentication.NewAccount;
import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.Activity.AbstractActivity;
import com.example.authentication.R;

public class SlideActivity extends AbstractActivity {

    public static ViewPager viewPager;
    private int position = 0;
    private TextView next, back, btnGetStarted;

    @Override
    public void UpdateLanguage(String language) {
        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();

        next.setText(resources.getString(R.string.next));
        btnGetStarted.setText(resources.getString(R.string.btnGetStarted));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_slide);

        viewPager=findViewById(R.id.viewpager);
        viewPager.setAdapter(new SlideFragmentStatePagerAdapter(getSupportFragmentManager()));

        if (isOpenAlready())
        {
            Intent intent=new Intent(SlideActivity.this, NewAccount.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK); // Intent.FLAG_ACTIVITY_CLEAR_TASK vs Intent.FLAG_ACTIVITY_TASK_ON_HOME
            startActivity(intent);
        }
        else
        {
            putBooleanPref("slide", false); // change to true to make walkthrough appear once only
        }
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);

        btnGetStarted = findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(), NewAccount.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK); //Intent.FLAG_ACTIVITY_CLEAR_TASK  vs Intent.FLAG_ACTIVITY_TASK_ON_HOME
            startActivity(intent);
        });

        next.setOnClickListener(v -> viewPager.postDelayed(() -> {
            viewPager.setCurrentItem(position++);
            // when go to the last page, next button will be disabled
            if (position == 3) { // 3 because position++
                next.setVisibility(View.GONE);
            } else {
                next.setVisibility(View.VISIBLE);
            }
        }, 100));

        back.setOnClickListener(v -> viewPager.postDelayed(() -> viewPager.setCurrentItem(position--), 100));
    }

    private boolean isOpenAlready() {
        return getBooleanPref("slide",false);
    }
}
