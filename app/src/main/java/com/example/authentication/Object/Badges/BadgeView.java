package com.example.authentication.Object.Badges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.authentication.Fragment.AbstractFragment;
import com.example.authentication.R;

public class BadgeView extends AbstractFragment {
    private static final String BADGE_ICON = "param1";
    private static final String BADGE_NAME = "param2";

    private int getBadgeIcon;
    private String getBadgeName;

    @Override
    public int getLayoutId() {
        return R.layout.badge_view;
    }

    @Override
    public void UpdateLanguage(String language) {

    }

    public BadgeView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public BadgeView newInstance(int badgeIcon, String badgeName) {
        BadgeView fragment = new BadgeView();
        Bundle args = new Bundle();
        args.putInt(BADGE_ICON, badgeIcon);
        args.putString(BADGE_NAME, badgeName);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getBadgeIcon = getArguments().getInt(BADGE_ICON);
            getBadgeName = getArguments().getString(BADGE_NAME);
        }
    }

    @Override
    protected BadgeView newInstance(String mParam1, String mParam2) {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Button backToBadgeCloseBtn = findViewById(R.id.badge_congratulation_close);
        Button backToBadgeContinueBtn = findViewById(R.id.badge_congratulation_continue);
        ImageView badgeIcon = findViewById(R.id.badge_congratulation_icon);
        TextView badgeName = findViewById(R.id.badge_congratulation_name);

        backToBadgeCloseBtn.setOnClickListener(v -> goBack());

        backToBadgeContinueBtn.setOnClickListener(v -> goBack());

        Glide.with(getContext()).load(getBadgeIcon).into(badgeIcon);
        badgeName.setText(getBadgeName);
    }
}
