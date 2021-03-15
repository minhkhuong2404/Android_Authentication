package com.example.authentication.Notification;

import android.graphics.drawable.Drawable;

public class NotificationItem {
    private String notificationTime;
    private String notificationInformation;
    private Drawable notificationIcon;

    public NotificationItem(String notificationTime, String notificationInformation, Drawable notificationIcon) {
        this.notificationTime = notificationTime;
        this.notificationInformation = notificationInformation;
        this.notificationIcon = notificationIcon;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    public String getNotificationInformation() {
        return notificationInformation;
    }

    public void setNotificationInformation(String notificationInformation) {
        this.notificationInformation = notificationInformation;
    }

    public Drawable getNotificationIcon() {
        return notificationIcon;
    }

    public void setNotificationIcon(Drawable notificationIcon) {
        this.notificationIcon = notificationIcon;
    }
}
