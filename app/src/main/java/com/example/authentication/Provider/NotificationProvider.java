package com.example.authentication.Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.authentication.Handler.NotificationHandler;

public class NotificationProvider extends ContentProvider {
    private static final String AUTHORITY = "com.example.authentication.Provider.NotificationProvider";
    private static final String NOTIFICATION_TABLE = "Notification";
    public static final int NOTIFICATION = 1;

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + NOTIFICATION_TABLE);

    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, NOTIFICATION_TABLE, NOTIFICATION);

    }
    private NotificationHandler myHandler;
    public NotificationProvider() {
    }

    @Override
    public boolean onCreate() {
        myHandler = new NotificationHandler(getContext(), null, null, 1);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(NotificationHandler.Table_notification);

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
            case NOTIFICATION:
                id = sqlDB.insertWithOnConflict(NotificationHandler.Table_notification,
                        null, values, SQLiteDatabase.CONFLICT_IGNORE);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "
                        + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(NOTIFICATION_TABLE + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myHandler.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case NOTIFICATION:
                rowsDeleted = sqlDB.delete(NotificationHandler.Table_notification,
                        selection,
                        selectionArgs);
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
            case NOTIFICATION:
                rowsUpdated =
                        sqlDB.update(NotificationHandler.Table_notification,
                                values,
                                selection,
                                selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: "
                        + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
