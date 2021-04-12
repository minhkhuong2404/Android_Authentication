package com.example.authentication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.authentication.Model.Handler.CourseHandler;
import com.example.authentication.Fragment.ManageAppFragment;
import com.example.authentication.Object.Course.Course;
import com.example.authentication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class Splash extends AbstractActivity implements CourseMVP.CourseView{

    private String category1, category2, study, collection;
    private ArrayList<String> allCategories;

    private ImageView icon, iconBackground;
    private TextView appName;
    private LottieAnimationView lottieAnimationView;
    private Animation topAnim, bottomAnim;
    private CourseMVP.CoursePresenter coursePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.splash_view);

        icon = findViewById(R.id.icon_splash);
        iconBackground = findViewById(R.id.background_icon_splash);
        appName = findViewById(R.id.name_splash);
        lottieAnimationView = findViewById(R.id.animation_view);
        coursePresenter = new CoursePresenterImpl(this, new GetWebServiceResponseDataImpl(getApplicationContext()));

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation );
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        icon.setAnimation(topAnim);
        iconBackground.setAnimation(topAnim);
        appName.setAnimation(bottomAnim);
        lottieAnimationView.setAnimation(bottomAnim);

        icon.animate().translationY(-1600).setDuration(1000).setStartDelay(4200);
        iconBackground.animate().translationY(-1600).setDuration(1000).setStartDelay(4200);
        appName.animate().translationY(1400).setDuration(1000).setStartDelay(4200);
        lottieAnimationView.animate().translationY(1400).setDuration(1000).setStartDelay(4200);

        new Handler().postDelayed(this::switchToHome,5000);
        try {
            allCategories = new ArrayList<>(coursePresenter.updateCourse());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        category1 = allCategories.get(0);
        category2 = allCategories.get(1);
        study = allCategories.get(2);
        collection = allCategories.get(3);
        coursePresenter.onFinished();
    }

    @Override
    public void UpdateLanguage(String language) {

    }

    @Override
    public void showMessage() {
        Toast.makeText(getApplicationContext(), "Welcome",Toast.LENGTH_SHORT).show();
    }

    private void switchToHome() {
        Intent switchToHome = new Intent(this, ManageAppFragment.class);
        switchToHome.putExtra("username", getIntent().getStringExtra("user"));
        switchToHome.putExtra("slidePager", "0");
        switchToHome.putExtra("slidePagerExplorer", "0");
        switchToHome.putExtra("generate1", category1);
        switchToHome.putExtra("generate2", category2);
        switchToHome.putExtra("study", study);
        switchToHome.putExtra("collection", collection);
        Log.d("Category Splash", category1 + " --- " + category2 + " --- " + study + " --- " + collection);
        startActivity(switchToHome);
        finish();
    }
}
