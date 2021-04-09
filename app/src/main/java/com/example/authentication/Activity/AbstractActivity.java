package com.example.authentication.Activity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authentication.Language.LocaleHelper;

abstract public class AbstractActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    protected void onStart() {
        super.onStart();
        String language = getStringPref("Locale.Helper.Selected.Language", "en");
        UpdateLanguage(language);
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
    protected String getStringPref(String key, String value) {
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.authentication_preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, value);
    }

    protected int getIntPref(String key, int value) {
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.authentication_preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, value);
    }

    protected boolean getBooleanPref(String key, boolean value) {
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.authentication_preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, value);
    }

    protected void putStringPref(String key, String value){
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.authentication_preferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    protected void putIntPref(String key, int value ) {
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.authentication_preferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt(key, value).apply();
    }

    protected void putBooleanPref(String key, boolean value) {
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.authentication_preferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value).apply();
    }

    abstract public void UpdateLanguage(String language);
}
