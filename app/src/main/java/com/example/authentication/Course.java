package com.example.authentication;

import android.graphics.drawable.Drawable;
import android.view.DragAndDropPermissions;

public class Course {
    private String courseName;
    private String beforeSalePrice;
    private String afterSalePrice;
    private String rate;
    private Drawable courseImage;

    public Course(String courseName, String beforeSalePrice, String afterSalePrice, String rate, Drawable courseImage) {
        this.courseName = courseName;
        this.beforeSalePrice = beforeSalePrice;
        this.afterSalePrice = afterSalePrice;
        this.rate = rate;
        this.courseImage = courseImage;
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

    public Drawable getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(Drawable courseImage) {
        this.courseImage = courseImage;
    }
}
