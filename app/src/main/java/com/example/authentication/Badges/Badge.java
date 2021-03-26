package com.example.authentication.Badges;

public class Badge {
    private String badgeName;
    private String badgeRequirement;
    private int badgeIcon;

    public Badge(String badgeName, String badgeRequirement, int badgeIcon) {
        this.badgeName = badgeName;
        this.badgeRequirement = badgeRequirement;
        this.badgeIcon = badgeIcon;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public String getBadgeRequirement() {
        return badgeRequirement;
    }

    public void setBadgeRequirement(String badgeRequirement) {
        this.badgeRequirement = badgeRequirement;
    }

    public int getBadgeIcon() {
        return badgeIcon;
    }

    public void setBadgeIcon(int badgeIcon) {
        this.badgeIcon = badgeIcon;
    }
}
