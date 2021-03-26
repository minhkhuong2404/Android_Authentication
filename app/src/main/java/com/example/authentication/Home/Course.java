package com.example.authentication.Home;

import android.graphics.drawable.Drawable;
import android.view.DragAndDropPermissions;

import static java.security.AccessController.getContext;

public class Course {
    private String courseName;
    private String beforeSalePrice;
    private String afterSalePrice;
    private String rate;
    private int courseImage;
    private String category;

    public Course(String courseName, String beforeSalePrice, String afterSalePrice, String rate, int courseImage, String category) {
        this.courseName = courseName;
        this.beforeSalePrice = beforeSalePrice;
        this.afterSalePrice = afterSalePrice;
        this.rate = rate;
        this.courseImage = courseImage;
        this.category = category;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getBeforeSalePrice() {
        return beforeSalePrice;
    }

    public void setBeforeSalePrice(String beforeSalePrice) {
        this.beforeSalePrice = beforeSalePrice;
    }

    public String getAfterSalePrice() {
        return afterSalePrice;
    }

    public void setAfterSalePrice(String afterSalePrice) {
        this.afterSalePrice = afterSalePrice;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(int courseImage) {
        this.courseImage = courseImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
