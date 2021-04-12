package com.example.authentication.Activity;

import android.content.Context;

import com.example.authentication.Model.Handler.CourseHandler;
import com.example.authentication.Object.Course.Course;

import java.util.List;

public class CoursePresenterImpl implements CoursePresenter{

    private CourseView courseView;
    private Context context;

    public CoursePresenterImpl(CourseView courseView, Context context){
        this.courseView = courseView;
        this.context = context;
    }

    @Override
    public List<Course> updateCourse(String whereToLoad) {
        if (courseView != null) {
            courseView.updateCourseData(new CourseHandler(context, null, null, 1).loadCourseHandler(whereToLoad));
        }
        return new CourseHandler(context, null, null, 1).loadCourseHandler(whereToLoad);
    }
}
