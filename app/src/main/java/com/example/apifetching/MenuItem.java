package com.example.apifetching;

import java.util.List;

public class MenuItem {
    private String headerName;
    private List<SubMenuItem> subMenuItems;

    public MenuItem(String headerName, List<SubMenuItem> subMenuItems) {
        this.headerName = headerName;
        this.subMenuItems = subMenuItems;
    }

    public String getHeaderName() {
        return headerName;
    }

    public List<SubMenuItem> getSubMenuItems() {
        return subMenuItems;
    }


}
