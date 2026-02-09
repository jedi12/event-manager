package ru.project.my.eventmanager.ui;

public class MenuItem {
    private final String title;
    private final String icon;
    private final String iconColor;
    private final String url;

    public MenuItem(String title, String icon, String iconColor, String url) {
        this.title = title;
        this.icon = icon;
        this.iconColor = iconColor;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public String getIconColor() {
        return iconColor;
    }

    public String getUrl() {
        return url;
    }
}
