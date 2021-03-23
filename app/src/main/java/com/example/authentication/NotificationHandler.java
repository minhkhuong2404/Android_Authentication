package com.example.authentication;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateUtils;

import androidx.annotation.Nullable;

import com.example.authentication.Notification.NotificationItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.chrono.ThaiBuddhistChronology;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationHandler extends SQLiteOpenHelper {
    private ContentResolver myNotification;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notification.db";
    public static final String Table_notification = "Notifications";
    public static final String Notification_item = "NotificationItem";
    public static final String Notification_time = "NotificationTime";
    public static final String Notification_icon = "NotificationIcon";

    public static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public NotificationHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        myNotification = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COURSE_TABLE = "CREATE TABLE IF NOT EXISTS " + Table_notification + "(" + Notification_time + " TEXT PRIMARY KEY,"
                + Notification_item + " TEXT, " + Notification_icon + " INT)";
        db.execSQL(CREATE_COURSE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_notification);
        onCreate(db);
    }

    public List<NotificationItem> loadDataHandler() throws ParseException {
        List<NotificationItem> notifications = new ArrayList<>();
        String[] projection = {Notification_time, Notification_item, Notification_icon};

        Cursor cursor;
        cursor = myNotification.query(NotificationProvider.CONTENT_URI, projection, null, null, Notification_time + " DESC");

        if (cursor.moveToFirst()) {
            do {
                int notificationIcon = cursor.getInt(2);
                String notificationItem = cursor.getString(1);
                String notificationTime = cursor.getString(0);

                String dateStr = notificationTime;
                Date date = inputFormat.parse(dateStr);
                String niceDateStr = (String) DateUtils.getRelativeTimeSpanString(date.getTime() , Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);

                notifications.add(new NotificationItem(niceDateStr, notificationItem, notificationIcon, dateStr));
            } while (cursor.moveToNext());
        }

        cursor.close();
//        db.close();
        return notifications;
    }

    public void addDataHandler(NotificationItem notificationItem) {
        ContentValues values = new ContentValues();
        values.put(Notification_time, notificationItem.getNotificationTime());
        values.put(Notification_item, notificationItem.getNotificationInformation());
        values.put(Notification_icon, notificationItem.getNotificationIcon());

        myNotification.insert(NotificationProvider.CONTENT_URI, values);
    }

    public boolean deleteDataHandler(String notification_time) {
        boolean result = false;
        String selection = "NotificationItem = \"" + notification_time +"\"";
        int rowsDeleted =
                myNotification.delete(NotificationProvider.CONTENT_URI,selection, null);
        if (rowsDeleted > 0)
            result = true;
        return result;
    }

    public boolean deleteAllDataHandler() {
        boolean result = false;
        int rowsDeleted =
                myNotification.delete(NotificationProvider.CONTENT_URI,null, null);
        if (rowsDeleted > 0)
            result = true;
        return result;
    }

    public boolean updateDataHandler(String notification_item, String notification_time, int notification_icon) {

        ContentValues values = new ContentValues();
        values.put(Notification_time, notification_time);
        values.put(Notification_item, notification_item);
        values.put(Notification_icon, notification_icon);

        boolean result = false;
        String selection = "Notification = \"" + notification_item + "\"";
        int rowsUpdated =
                myNotification.update(NotificationProvider.CONTENT_URI,values,selection,null);
        if (rowsUpdated > 0)
            result = true;
        return result;
    }
}
