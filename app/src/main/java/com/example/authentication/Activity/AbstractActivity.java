package com.example.authentication.Activity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authentication.Language.LocaleHelper;

abstract public class AbstractActivity extends AppCompatActivity {

    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPreferences.getString("Locale.Helper.Selected.Language", "en");
        UpdateLanguage(language);
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    abstract public void UpdateLanguage(String language);
}
