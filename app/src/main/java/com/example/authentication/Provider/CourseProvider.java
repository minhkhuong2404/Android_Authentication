package com.example.authentication.Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.authentication.Handler.CourseHandler;

public class CourseProvider extends ContentProvider {
    private static final String AUTHORITY = "com.example.authentication.Provider.CourseProvider";
    private static final String COURSE_TABLE = "Courses";
    public static final int COURSES = 1;
    public static final int COURSES_CATEGORY = 2;

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + COURSE_TABLE);

    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, COURSE_TABLE, COURSES);
        sURIMatcher.addURI(AUTHORITY, COURSE_TABLE + "/#",
                COURSES_CATEGORY);
    }
    private CourseHandler myHandler;
    public CourseProvider() {
    }

    private SQLiteDatabase db;
    @Override
    public boolean onCreate() {
        myHandler = new CourseHandler(getContext(), null, null, 1);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(CourseHandler.Table_course);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case COURSES_CATEGORY:
                queryBuilder.appendWhere(CourseHandler.Category + "="
                        + uri.getLastPathSegment());
                break;
            case COURSES:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor = queryBuilder.query(myHandler.getReadableDatabase(),projection,
                selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myHandler.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case COURSES:
                id = sqlDB.insertWithOnConflict(CourseHandler.Table_course,
                        null, values, SQLiteDatabase.CONFLICT_IGNORE);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "
                        + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(COURSE_TABLE + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myHandler.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case COURSES:
                rowsDeleted = sqlDB.delete(CourseHandler.Table_course,
                        selection,
                        selectionArgs);
                break;
            case COURSES_CATEGORY:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted =
                            sqlDB.delete(CourseHandler.Table_course,
                                    myHandler.Course_name + "=" + id,
                                    null);
                } else {
                    rowsDeleted =
                            sqlDB.delete(CourseHandler.Table_course,
                                    myHandler.Course_name + "=" + id
                                            + " and " + selection,
                                    selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " +
                        uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myHandler.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case COURSES:
                rowsUpdated =
                        sqlDB.update(CourseHandler.Table_course,
                                values,
                                selection,
                                selectionArgs);
                break;
            case COURSES_CATEGORY:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated =
                            sqlDB.update(CourseHandler.Table_course,
                                    values,
                                    CourseHandler.Course_name + "=" + id,
                                    null);
                } else {
                    rowsUpdated =
                            sqlDB.update(CourseHandler.Table_course,
                                    values,
                                    CourseHandler.Course_name + "=" + id
                                            + " and "
                                            + selection,
                                    selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "
                        + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
