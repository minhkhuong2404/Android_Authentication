package com.example.authentication;

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

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.authentication.Handler.CourseHandler;

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
import java.util.Random;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class Splash extends AppCompatActivity {

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
    private AsyncTaskSplash asyncTask;

    ImageView icon, iconBackground;
    TextView appName;
    LottieAnimationView lottieAnimationView;
    Animation topAnim, bottomAnim;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.splash_view);

        asyncTask = new AsyncTaskSplash();
        asyncTask.execute();

        icon = findViewById(R.id.icon_splash);
        iconBackground = findViewById(R.id.background_icon_splash);
        appName = findViewById(R.id.name_splash);
        lottieAnimationView = findViewById(R.id.animation_view);

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

    }

    private class AsyncTaskSplash extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... objects) {
            getWebServiceResponseData();
            return "Hello";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute("");
            asyncTask.cancel(true);
        }
    }

    public void getWebServiceResponseData() {
        try {
            url = new URL(path);
            Log.d("REST", "ServerData: " + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            Log.d("REST", "Response code: " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                // Reading response from input Stream
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String output;
                response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        responseText = response.toString();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("JSON", responseText);
        editor.apply();

        //Call ServerData() method to call webservice and store result in response
        //  response = service.ServerData(path, postDataParams);
//        Log.d("REST", "data:" + responseText);
        try {
            JSONArray jsonarray = new JSONArray(responseText);
            CourseHandler dbHandler = new CourseHandler(this, null, null, 1);
            dbHandler.deleteAllCourseHandler();

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String CourseCategory = jsonobject.getString("CourseCategory");
                category.add(CourseCategory);
            }
            Collections.shuffle(category);
            Set<String> foo = new HashSet<String>(category);
            String[] uniqueCategory = new String[foo.size()];
            foo.toArray(uniqueCategory);

            category1 = uniqueCategory[0];
            category2 = uniqueCategory[1];
            study = uniqueCategory[2];
            collection = uniqueCategory[3];

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void switchToHome() {
        Intent switchToHome = new Intent(this, MainActivity.class);
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
