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
import com.example.authentication.R;

public class BadgeView extends Fragment {
    private static final String BADGE_ICON = "param1";
    private static final String BADGE_NAME = "param2";

    private int getBadgeIcon;
    private String getBadgeName;

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
    public static BadgeView newInstance(int badgeIcon, String badgeName) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.badge_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Button backToBadgeCloseBtn = view.findViewById(R.id.badge_congratulation_close);
        Button backToBadgeContinueBtn = view.findViewById(R.id.badge_congratulation_continue);
        ImageView badgeIcon = view.findViewById(R.id.badge_congratulation_icon);
        TextView badgeName = view.findViewById(R.id.badge_congratulation_name);

        backToBadgeCloseBtn.setOnClickListener(v -> backToBadge());

        backToBadgeContinueBtn.setOnClickListener(v -> backToBadge());

        Glide.with(getContext()).load(getBadgeIcon).into(badgeIcon);
        badgeName.setText(getBadgeName);
    }

    private void backToBadge() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
