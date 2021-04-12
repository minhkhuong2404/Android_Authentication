package com.example.authentication.Activity;

import android.content.Context;
import android.util.Log;

import com.example.authentication.Model.Handler.CourseHandler;
import com.example.authentication.Object.Course.Course;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoursePresenterImpl implements CourseMVP.CoursePresenter, CourseMVP.GetWebServiceResponseData.OnFinishedListener{

    private CourseMVP.CourseView courseView;
    private CourseMVP.GetWebServiceResponseData getWebServiceResponseData;
    private ArrayList<String> allCourse;

    public CoursePresenterImpl(CourseMVP.CourseView courseView, CourseMVP.GetWebServiceResponseData getWebServiceResponseData){
        this.courseView = courseView;
        this.getWebServiceResponseData = getWebServiceResponseData;
    }

    @Override
    public ArrayList<String> updateCourse(){
        allCourse = new ArrayList<>(getWebServiceResponseData.getWebServiceResponseData());
        Log.d("All courses", Arrays.toString(allCourse.toArray()));
        return allCourse;
    }

    @Override
    public void onFinished() {
        courseView.showMessage();
    }
}
