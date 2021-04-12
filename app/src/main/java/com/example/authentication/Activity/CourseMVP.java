package com.example.authentication.Activity;

import java.util.ArrayList;

public interface CourseMVP {

    interface GetWebServiceResponseData {
        interface OnFinishedListener {
            void onFinished();
        }

        ArrayList<String> getWebServiceResponseData();
        ArrayList<String> getRandomCategory(ArrayList<String> categoryList);
    }

    interface CoursePresenter {
        ArrayList<String> updateCourse() throws InterruptedException;
        void onFinished();
    }

    interface CourseView {
        void showMessage();
    }
}
