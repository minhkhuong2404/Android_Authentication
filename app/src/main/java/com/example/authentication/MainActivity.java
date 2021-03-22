package com.example.authentication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.authentication.Explorer.Explorer;
import com.example.authentication.Home.Home;
import com.example.authentication.Notification.Notification;
import com.example.authentication.Profile.Profile;
import com.example.authentication.Search.Search;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        bottomNavigation = findViewById(R.id.bottomNav_view);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        String username = getIntent().getStringExtra("username");
        openFragment(Home.newInstance(username, ""));
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.myContainer, fragment);
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
                            openFragment(Home.newInstance("", ""));
                            return true;
                        case R.id.navigation_search:
                            openFragment(Explorer.newInstance("", ""));
                            return true;
                        case R.id.navigation_notification:
                            openFragment(Notification.newInstance("", ""));
                            return true;
                        case R.id.navigation_profile:
                            openFragment(Profile.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };
}
