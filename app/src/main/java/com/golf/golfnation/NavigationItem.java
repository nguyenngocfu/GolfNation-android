package com.golf.golfnation;

/**
 * Created by Ngọc Nguyễn on 12/19/2014.
 */
public class NavigationItem {
    private int index;
    private String title;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    private String imageURL;
    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    private int resId;
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public NavigationItem(String title, int resId) {
        this.title = title;
        this.resId = resId;
    }
}
