package com.example.authentication.Handler;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.authentication.Home.Course;
import com.example.authentication.Provider.CourseProvider;

import java.util.ArrayList;
import java.util.List;

public class CourseHandler extends SQLiteOpenHelper {
    private ContentResolver myCR;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "course.db";
    public static final String Table_course = "Courses";
    public static final String Course_name = "CourseName";
    public static final String Before_sale_price = "BeforeSalePrice";
    public static final String After_sale_price = "AfterSalePrice";
    public static final String Rate = "Rate";
    public static final String Category = "Category";
    public static final String Course_image = "CourseImage";

    public CourseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        myCR = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COURSE_TABLE = "CREATE TABLE IF NOT EXISTS " + Table_course + "(" + Course_name + " TEXT PRIMARY KEY,"
                + Before_sale_price + " TEXT," + After_sale_price + " TEXT," + Rate + " TEXT," + Course_image + " INT," + Category + " TEXT)";
        db.execSQL(CREATE_COURSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_course);
        onCreate(db);
    }

    public List<Course> loadDataHandler(String course) {
        List<Course> courses = new ArrayList<>();
        String[] projection = {Course_name, Before_sale_price, After_sale_price, Rate, Course_image, Category};
        String selection = "Category = \"" + course + "\"";

//        String query = "";
//        if (!course.equals("All")) {
//            query = "SELECT * FROM " + Table_course + " WHERE " + Category + " = " + "'" + course + "'";
//        } else {
//            query = "SELECT * FROM " + Table_course;
//        }

//        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor;
        if (!course.equals("All")){
            cursor = myCR.query(CourseProvider.CONTENT_URI, projection, selection, null, null);
        } else  {
            cursor = myCR.query(CourseProvider.CONTENT_URI, projection, null, null, null);
        }

        if (cursor.moveToFirst()) {
            do {
                String courseName = cursor.getString(0);
                String beforeSalePrice = cursor.getString(1);
                String afterSalePrice = cursor.getString(2);
                String rate = cursor.getString(3);
                int courseImage = cursor.getInt(4);
                String category = cursor.getString(5);
                courses.add(new Course(courseName, beforeSalePrice, afterSalePrice, rate, courseImage, category));
            } while (cursor.moveToNext());
        }

        cursor.close();
//        db.close();
        return courses;
    }

    public void addDataHandler(Course course) {
        ContentValues values = new ContentValues();
        values.put(Course_name, course.getCourseName());
        values.put(Before_sale_price, course.getBeforeSalePrice());
        values.put(After_sale_price, course.getAfterSalePrice());
        values.put(Rate, course.getRate());
        values.put(Course_image, course.getCourseImage());
        values.put(Category, course.getCategory());
        myCR.insert(CourseProvider.CONTENT_URI, values);
    }

    public boolean deleteDataHandler(String course_name) {
        boolean result = false;
        String selection = "Course_name = \"" + course_name +"\"";
        int rowsDeleted =
                myCR.delete(CourseProvider.CONTENT_URI,selection, null);
        if (rowsDeleted > 0)
            result = true;
        return result;
    }

    public boolean deleteAllDataHandler() {
        boolean result = false;
        int rowsDeleted =
                myCR.delete(CourseProvider.CONTENT_URI,null, null);
        if (rowsDeleted > 0)
            result = true;
        return result;
    }

    public boolean updateDataHandler(String course_name, String before_sale_price, String after_sale_price, String rate, int course_image, String category) {

        ContentValues values = new ContentValues();
        values.put(Course_name, course_name);
        values.put(Before_sale_price, before_sale_price);
        values.put(After_sale_price, after_sale_price);
        values.put(Rate, rate);
        values.put(Course_image, course_image);
        values.put(Category, category);

        boolean result = false;
        String selection = "Course_name = \"" + course_name + "\"";
        int rowsUpdated =
                myCR.update(CourseProvider.CONTENT_URI,values,selection,null);
        if (rowsUpdated > 0)
            result = true;
        return result;
    }
}
