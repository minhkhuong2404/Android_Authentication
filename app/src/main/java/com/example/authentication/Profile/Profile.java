package com.example.authentication.Profile;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
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

import com.example.authentication.LogIn;
import com.example.authentication.MyCourses.MyCourses;
import com.example.authentication.R;
import com.example.authentication.Search.Search;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

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

    public Profile() {
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
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLogIn();
            }
        });

        historyActivity = view.findViewById(R.id.setting_activity_history);
        historyActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switchToMyCourse();
            }
        });

        notificationActivity = view.findViewById(R.id.setting_notifications);
        notificationActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToNotification();
            }
        });

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
        Fragment fragment = new com.example.authentication.Notification.Notification();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.myContainer, fragment, "notification");
        fragmentTransaction.commit();
    }

}
