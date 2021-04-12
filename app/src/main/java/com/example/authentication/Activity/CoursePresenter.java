package com.example.authentication.Activity;

import com.example.authentication.Object.Course.Course;

import java.util.List;

public interface CoursePresenter {
    List<Course> updateCourse(String whereToLoad);
}
