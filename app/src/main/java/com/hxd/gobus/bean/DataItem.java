package com.hxd.gobus.bean;

/**
 * @author: wangqingbin
 * @date: 2019/7/30 19:24
 */

public class DataItem {

    private String title;
    private int icon;

    public DataItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
