package com.example.authentication.Notification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.authentication.Home.CourseAdapter;
import com.example.authentication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Notification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notification extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvNotification;
    private NotificationAdapter notificationAdapter;
    private List<NotificationItem> mNotifications;
    public Notification() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Notification.
     */
    // TODO: Rename and change types and number of parameters
    public static Notification newInstance(String param1, String param2) {
        Notification fragment = new Notification();
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
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvNotification = view.findViewById(R.id.rv_notification);

        mNotifications = new ArrayList<>();

        mNotifications.add(new NotificationItem("Just now", "You got a new message from Kevin", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mNotifications.add(new NotificationItem("2 hours ago", "Please update your information", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mNotifications.add(new NotificationItem("4 hours ago", "You have a new video course", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mNotifications.add(new NotificationItem("4 hours ago", "You have a new badge", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mNotifications.add(new NotificationItem("7 hours ago", "You have a new video course", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mNotifications.add(new NotificationItem("4 hours ago", "You got a new message from Dave", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mNotifications.add(new NotificationItem("8 hours ago", "You got a new message from Timmy", getContext().getResources().getDrawable(R.drawable.orange_background)));

        notificationAdapter = new NotificationAdapter(getContext(), mNotifications);

        rvNotification.setAdapter(notificationAdapter);

        LinearLayoutManager linearLayoutManagerBusiness = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvNotification.setLayoutManager(linearLayoutManagerBusiness);
    }
}
