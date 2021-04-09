package com.example.authentication.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.authentication.R;

abstract public class AbstractFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private View rootView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2, mParam3, mParam4;

    abstract public int getLayoutId();
    abstract public void UpdateLanguage(String language);

    public AbstractFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sharedPreferences = getContext().getSharedPreferences("com.example.authentication_preferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        rootView = inflater.inflate(getLayoutId(), container, false);
        return rootView;
    }

    public void onStart() {
        super.onStart();
        String language = getStringPref("Locale.Helper.Selected.Language", "en");
        UpdateLanguage(language);
    }

    public <T extends AbstractFragment> void switchToOtherFragment(T fragment, String tag, String mParam1, String mParam2){
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_out);
        fragmentTransaction.add(R.id.myContainer, fragment.newInstance(mParam1, mParam2), tag);
        fragmentTransaction.addToBackStack(tag).commit();
    }
    public void goBack() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    public <T extends View> void startAnimation(T view) {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        a.reset();
        view.clearAnimation();
        view.startAnimation(a);
    }

    protected  <T extends View> T findViewById(int id) {
        return getRootView().findViewById(id);
    }

    public View getRootView() {
        return rootView;
    }

    protected String getStringPref(String key, String value) {
        return sharedPreferences.getString(key, value);
    }

    protected int getIntPref(String key, int value) {
        return sharedPreferences.getInt(key, value);
    }

    protected boolean getBooleanPref(String key, boolean value) {
        return sharedPreferences.getBoolean(key, value);
    }

    protected void putStringPref(String key, String value){
        editor.putString(key, value).apply();
    }

    protected void putIntPref(String key, int value ) {
        editor.putInt(key, value).apply();
    }

    protected void putBooleanPref(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    protected abstract Fragment newInstance(String mParam1, String mParam2);
}
