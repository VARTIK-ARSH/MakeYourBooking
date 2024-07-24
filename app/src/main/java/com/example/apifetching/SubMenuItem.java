package com.example.apifetching;

public class SubMenuItem {
    private String menuName;
    private String menuIconUrl;

    public SubMenuItem(String menuName, String menuIconUrl) {
        this.menuName = menuName;
        this.menuIconUrl = menuIconUrl;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getMenuIconUrl() {
        return menuIconUrl;
    }
}
