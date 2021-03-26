package com.example.authentication.Setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.authentication.Authentication.LogIn;
import com.example.authentication.MyCourses.MyCourses;
import com.example.authentication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Setting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Setting extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button signOutButton;
    private TextView historyActivity;
    private TextView notificationActivity;
    private TextView profileActivity;
    private TextView helpActivity;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
        getActivity().startActivity(i);
    }

    private void switchToProfile() {
        Fragment fragment = new Profile();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.myContainer, fragment, "profile");
        fragmentTransaction.addToBackStack("profile");
        fragmentTransaction.commit();
    }

    private void switchToLogIn() {
        Intent switchToLogIn = new Intent(getActivity(), LogIn.class);
        startActivity(switchToLogIn);
    }

    private void switchToMyCourse() {
        Fragment fragment = new MyCourses();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.myContainer, fragment, "my_course");
        fragmentTransaction.addToBackStack("my_course");
        fragmentTransaction.commit();
    }

    private void switchToNotification() {
        Fragment fragment = new com.example.authentication.Setting.Notification();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.myContainer, fragment, "notification");
        fragmentTransaction.addToBackStack("notification");
        fragmentTransaction.commit();
    }

}
