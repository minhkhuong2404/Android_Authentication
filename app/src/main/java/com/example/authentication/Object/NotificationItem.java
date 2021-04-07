package com.example.authentication.Object;

import android.graphics.drawable.Drawable;

public class NotificationItem {
    private String notificationTime;
    private String notificationInformation;
    private int notificationIcon;
    private String notificationCreationTime;

    public NotificationItem(String notificationTime, String notificationInformation, int notificationIcon, String notificationCreationTime) {
        this.notificationTime = notificationTime;
        this.notificationInformation = notificationInformation;
        this.notificationIcon = notificationIcon;
        this.notificationCreationTime = notificationCreationTime;
    }

    public String getNotificationCreationTime() {
        return notificationCreationTime;
    }

    public void setNotificationCreationTime(String notificationCreationTime) {
        this.notificationCreationTime = notificationCreationTime;
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

    public int getNotificationIcon() {
        return notificationIcon;
    }

    public void setNotificationIcon(int notificationIcon) {
        this.notificationIcon = notificationIcon;
    }
}
