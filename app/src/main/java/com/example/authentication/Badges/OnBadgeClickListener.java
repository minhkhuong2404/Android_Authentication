package com.example.authentication.Badges;

import android.widget.ImageView;

import com.example.authentication.Home.Course;

public interface OnBadgeClickListener {
    void onViewClicked(Badge badge);
    void onBadgeClicked(ImageView imageView);
}
