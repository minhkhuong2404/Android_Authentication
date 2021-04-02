package com.example.authentication.Walkthrough;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.authentication.Authentication.NewAccount;
import com.example.authentication.Handler.CourseHandler;
import com.example.authentication.Home.Course;
import com.example.authentication.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.net.ssl.HttpsURLConnection;

import static android.view.View.GONE;

public class SlideActivity extends AppCompatActivity {

    public static ViewPager viewPager;
    private SlideFragmentStatePagerAdapter adapter;
    private int position = 0;
    private SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_slide);

        adapter=new SlideFragmentStatePagerAdapter(getSupportFragmentManager());

        viewPager=findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        if (isOpenAlready())
        {
            Intent intent=new Intent(SlideActivity.this, NewAccount.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK); // Intent.FLAG_ACTIVITY_CLEAR_TASK vs Intent.FLAG_ACTIVITY_TASK_ON_HOME
            startActivity(intent);
        }
        else
        {
            SharedPreferences.Editor editor1=getSharedPreferences("slide",MODE_PRIVATE).edit();
            editor1.putBoolean("slide",false); // change to true to make walkthrough appear once only
            editor1.apply();
        }
        TextView next = findViewById(R.id.next);
        TextView back = findViewById(R.id.back);

        TextView btnGetStarted = findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(v -> {
//            String json = gson.toJson(categories);
//            editor.putString("GSON", json);
//            editor.apply();

            Intent intent=new Intent(getApplicationContext(), NewAccount.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK); //Intent.FLAG_ACTIVITY_CLEAR_TASK  vs Intent.FLAG_ACTIVITY_TASK_ON_HOME
            getApplicationContext().startActivity(intent);
        });

        next.setOnClickListener(v -> viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(position++);
                // when go to the last page, next button will be disabled

                if (position == 3) { // 3 because position++
                    next.setVisibility(View.GONE);
                } else {
                    next.setVisibility(View.VISIBLE);
                }
            }
        }, 100));

        back.setOnClickListener(v -> viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(position--);
            }
        }, 100));


    }

    private boolean isOpenAlready() {
        SharedPreferences sharedPreferences=getSharedPreferences("slide",MODE_PRIVATE);
        boolean result=sharedPreferences.getBoolean("slide",false);
        return result;

    }
}
