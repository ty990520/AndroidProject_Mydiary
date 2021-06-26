package com.cookandroid.mydiary1;

import android.widget.ImageView;

public class ListViewItem {
    private String menuName;
    private String menuSummary;
    private int resId;

    public ListViewItem(String menuName, String menuSummary,int resId) {
        this.menuName = menuName;
        this.menuSummary = menuSummary;
        this.resId = resId;
    }

    public ListViewItem(String menuName,int resId) {
        this.menuName = menuName;
        this.resId = resId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuSummary() {
        return menuSummary;
    }

    public void setMenuSummary(String menuSummary) {
        this.menuSummary = menuSummary;
    }


    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
