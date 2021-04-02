package com.example.authentication.MyCourses;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.authentication.Home.Course;

public interface OnItemClickedListener {
    void onAddClicked(TextView textView);
    void onViewClicked(Course course);
    void onCourseClicked(ImageView imageView);
}
