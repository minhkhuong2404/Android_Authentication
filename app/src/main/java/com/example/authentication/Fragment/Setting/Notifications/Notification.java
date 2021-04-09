package com.example.authentication.Fragment.Setting.Notifications;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.authentication.Adapter.RecyclerView.NotificationRecyclerViewAdapter;
import com.example.authentication.Fragment.AbstractFragment;
import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.Object.NotificationItem;
import com.example.authentication.Model.Handler.NotificationHandler;
import com.example.authentication.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Notification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notification extends AbstractFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2;

    private RecyclerView rvNotification;
    private TextView notificationTextView;
    private NotificationRecyclerViewAdapter notificationAdapter;
    private List<NotificationItem> mNotifications = new ArrayList<>();;
    private Button backButton;

    @Override
    public int getLayoutId() {
        return R.layout.notification_setting;
    }

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
    public Notification newInstance(String param1, String param2) {
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
    public void UpdateLanguage(String language) {
        Context context = LocaleHelper.setLocale(getContext(), language);
        Resources resources = context.getResources();

        notificationTextView.setText(resources.getString(R.string.notifications));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvNotification = findViewById(R.id.rv_notification_setting);
        notificationTextView = findViewById(R.id.notification_setting);

        try {
            mNotifications = new NotificationHandler(getContext(), null, null, 1).loadNotificationHandler();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        notificationAdapter = new NotificationRecyclerViewAdapter(getContext(), mNotifications);
        notificationAdapter.setData(mNotifications);
        rvNotification.setAdapter(notificationAdapter);

        rvNotification.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        backButton = findViewById(R.id.back_btn_notification_setting);
        backButton.setOnClickListener(v -> goBack());
    }
}
