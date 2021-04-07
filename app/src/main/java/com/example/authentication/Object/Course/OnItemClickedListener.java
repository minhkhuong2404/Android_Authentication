package com.example.authentication.Object.Course;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.authentication.Object.Course.Course;

public interface OnItemClickedListener {
    void onAddClicked(TextView textView);
    void onViewClicked(Course course);
    void onCourseClicked(ImageView imageView);
}
