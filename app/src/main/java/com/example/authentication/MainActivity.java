package com.example.authentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.authentication.Explorer.Explorer;
import com.example.authentication.Handler.CourseHandler;
import com.example.authentication.Home.Course;
import com.example.authentication.Home.Home;
import com.example.authentication.Notification.Notification;
import com.example.authentication.Service.MusicService;
import com.example.authentication.Setting.Setting;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    int count = 0;
    private String responseText;
    private boolean imageOk;
    private String newCourseImage;
    private AsyncTaskExample asyncTask;
    private SharedPreferences sharedPreferences ;
    private int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.main_activity);
        playSong();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Finished", "");
        editor.apply();

        asyncTask = new AsyncTaskExample();
        asyncTask.execute();

        bottomNavigation = findViewById(R.id.bottomNav_view);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        new Handler().postDelayed(() -> {
            openFragment(Home.newInstance(getIntent().getStringExtra("username"), getIntent().getStringExtra("slidePager"),
                    getIntent().getStringExtra("generate1"),
                    getIntent().getStringExtra("generate2"))
                    , "Home");
        } , 0);



    }

    public void openFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out);
        transaction.replace(R.id.myContainer, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            openFragment(Home.newInstance(getIntent().getStringExtra("username"),"", getIntent().getStringExtra("generate1"), getIntent().getStringExtra("generate2")), "Home");
                            Log.d("Category Main Home", getIntent().getStringExtra("generate1") + " --- " + getIntent().getStringExtra("generate2"));

                            return true;

                        case R.id.navigation_search:
                            if (count == 0) {
                                openFragment(Explorer.newInstance("", getIntent().getStringExtra("slidePagerExplorer")), "Search");
                                count += 1;
                            } else {
                                openFragment(Explorer.newInstance("", ""), "Search");
                            }
                            return true;
                        case R.id.navigation_notification:
                            openFragment(Notification.newInstance("", ""), "Notification");
                            return true;
                        case R.id.navigation_profile:
                            Log.d("Category Main Setting", getIntent().getStringExtra("study") + " --- " + getIntent().getStringExtra("collection"));
                            openFragment(Setting.newInstance(getIntent().getStringExtra("study"), getIntent().getStringExtra("collection")), "Setting");
                            return true;
                    }
                    return false;
                }
            };

    private class AsyncTaskExample extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... objects) {
            getWebServiceResponseData();
            return null;
        }

        @Override
        protected void onPostExecute(String a) {
            super.onPostExecute("");
            Log.d("Finished", "All data");
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("Home");
            if (fragment instanceof Home){
                ((Home)fragment).loading();
                ((Home)fragment).updateList();
            }

        }
    }

    public void playSong()  {
        // Create Intent object for PlaySongService.
        Intent myIntent = new Intent(MainActivity.this, MusicService.class);

        // Call startService with Intent parameter.
        this.startService(myIntent);
    }

    // This method is called when users click on the Stop button.
    public void stopSong( )  {

        // Create Intent object
        Intent myIntent = new Intent(MainActivity.this, MusicService.class);
        this.stopService(myIntent);
    }

    public void getWebServiceResponseData() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        responseText = sharedPreferences.getString("JSON", "");
        //Call ServerData() method to call webservice and store result in response
        //  response = service.ServerData(path, postDataParams);
        try {
            JSONArray jsonarray = new JSONArray(responseText);
            CourseHandler dbHandler = new CourseHandler(this, null, null, 1);
            dbHandler.deleteAllCourseHandler();
            ArrayList<Integer> idPhoto = new ArrayList<>();
            for (int i = 1; i <= 1800;i++) {
                idPhoto.add(i);
            }
            Collections.shuffle(idPhoto);
            for (int i = 0; i < jsonarray.length(); i++) {
                imageOk = false;
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                String courseName = jsonobject.getString("CourseName");
                String CourseBeforeSalePrice = jsonobject.getString("CourseBeforeSalePrice");
                String CourseAfterSalePrice = jsonobject.getString("CourseAfterSalePrice");
                String Rate = jsonobject.getString("Rate");
                String CourseCategory = jsonobject.getString("CourseCategory");
                String CourseImage = jsonobject.getString("CourseImage");
                String[] element = CourseImage.split("/");

                while (!imageOk) {

                    String newIdPhoto = String.valueOf(idPhoto.get(index));
                    element[4] = newIdPhoto;
                    newCourseImage = TextUtils.join("/",element);
                    URLConnection connection = new URL(newCourseImage).openConnection();
                    String contentType = connection.getHeaderField("Content-Type");
                    imageOk = contentType.startsWith("image/");
                    index += 1;
                }
                if ((i+1) % 10 == 0) {
                    Log.d("Changed", "Data");
                }
//                Log.d("New id", newCourseImage);

                dbHandler.addCourseHandler(new Course(courseName, CourseBeforeSalePrice, CourseAfterSalePrice, Rate,  newCourseImage, CourseCategory));
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Finished", "");
            editor.apply();

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asyncTask.cancel(true);
        stopSong();
        Log.d("Async", "Cancelled async");
    }
}
