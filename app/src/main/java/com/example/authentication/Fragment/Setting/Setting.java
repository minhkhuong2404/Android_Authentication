package com.example.authentication.Fragment.Setting;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.authentication.Activity.Authentication.LogIn;
import com.example.authentication.Fragment.AbstractFragment;
import com.example.authentication.Fragment.Setting.Notifications.Notification;
import com.example.authentication.Fragment.Setting.MyAccount.MyAccount;
import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.Fragment.Setting.ActivityHistory.ActivityHistory;
import com.example.authentication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Setting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Setting extends AbstractFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2;
    private Button signOutButton;
    private TextView settings, historyActivity, notificationActivity, profileActivity, helpActivity, billingActivity, securityActivity;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
    }

    public Setting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Setting newInstance(String param1, String param2) {
        Setting fragment = new Setting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d("Category Splash", mParam1 + " --- " + mParam2);

    }

    @Override
    public void UpdateLanguage(String language) {
        Context context = LocaleHelper.setLocale(getContext(), language);
        Resources resources = context.getResources();

        settings.setText(resources.getString(R.string.settings));
        profileActivity.setText(resources.getString(R.string.setting_my_account));
        notificationActivity.setText(resources.getString(R.string.setting_notifications));
        historyActivity.setText(resources.getString(R.string.setting_activity_history));
        billingActivity.setText(resources.getString(R.string.setting_billing_and_payment));
        securityActivity.setText(resources.getString(R.string.setting_security_and_privacy));
        helpActivity.setText(resources.getString(R.string.setting_help));
        signOutButton.setText(resources.getString(R.string.sign_out_btn));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        settings = view.findViewById(R.id.setting);
        billingActivity = view.findViewById(R.id.setting_billing_and_payment);
        securityActivity = view.findViewById(R.id.setting_security_and_privacy);

        signOutButton = view.findViewById(R.id.sign_out_btn);
        signOutButton.setOnClickListener(v -> switchToLogIn());

        historyActivity = view.findViewById(R.id.setting_activity_history);
        historyActivity.setOnClickListener(v -> switchToMyCourse());

        notificationActivity = view.findViewById(R.id.setting_notifications);
        notificationActivity.setOnClickListener(v -> switchToNotification());

        profileActivity = view.findViewById(R.id.setting_my_account);
        profileActivity.setOnClickListener(v -> switchToProfile());

        helpActivity = view.findViewById(R.id.setting_help);
        helpActivity.setOnClickListener(v -> switchToHelp());

    }

    private void switchToHelp() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www.google.com"));
        requireActivity().startActivity(i);
    }

    private void switchToProfile() {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_out);
        fragmentTransaction.add(R.id.myContainer, MyAccount.newInstance(mParam1, mParam2), "profile");
        fragmentTransaction.addToBackStack("profile").commit();
    }

    private void switchToLogIn() {
        startActivity(new Intent(getActivity(), LogIn.class));
        getActivity().finish();
    }

    private void switchToMyCourse() {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_out);
        fragmentTransaction.add(R.id.myContainer, ActivityHistory.newInstance(mParam1, mParam2), "my_course");
        fragmentTransaction.addToBackStack("my_course").commit();
    }

    private void switchToNotification() {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_out);
        fragmentTransaction.add(R.id.myContainer, new Notification(), "notification");
        fragmentTransaction.addToBackStack("notification").commit();
    }

}
