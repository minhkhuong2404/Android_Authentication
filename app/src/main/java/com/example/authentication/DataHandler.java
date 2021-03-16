package com.example.authentication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class DataHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "course.db";
    public static final String Table_couse = "Courses";
    public static final String Course_name = "CourseName";
    public static final String Before_sale_price = "BeforeSalePrice";
    public static final String After_sale_price = "AfterSalePrice";
    public static final String Rate = "Rate";
    public static final String Category = "Category";

    public DataHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COURSE_TABLE = "CREATE TABLE IF NOT EXISTS " + Table_couse + "(" + Course_name + " TEXT PRIMARY KEY,"
                + Before_sale_price + " TEXT," + After_sale_price + " TEXT," + Rate + " TEXT," + Category + " TEXT)";
        db.execSQL(CREATE_COURSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_couse);
        onCreate(db);
    }

    public List<Course> loadDataHandler(String course) {
        List<Course> courses = new ArrayList<>();
        String query = "";
        if (!course.equals("All")) {
            query = "SELECT * FROM " + Table_couse + " WHERE " + Category + " = " + "'" + course + "'";
        } else {
            query = "SELECT * FROM " + Table_couse;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String courseName = cursor.getString(0);
                String beforeSalePrice = cursor.getString(1);
                String afterSalePrice = cursor.getString(2);
                String rate = cursor.getString(3);
                String category = cursor.getString(4);
                courses.add(new Course(courseName, beforeSalePrice, afterSalePrice, rate, R.drawable.orange_background, category));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return courses;
    }

    public void addDataHandler(Course course) {
        ContentValues values = new ContentValues();
        values.put(Course_name, course.getCourseName());
        values.put(Before_sale_price, course.getBeforeSalePrice());
        values.put(After_sale_price, course.getAfterSalePrice());
        values.put(Rate, course.getRate());
        values.put(Category, course.getCategory());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insertWithOnConflict(Table_couse, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }
}
