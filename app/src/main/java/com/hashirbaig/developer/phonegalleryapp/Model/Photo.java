package com.hashirbaig.developer.phonegalleryapp.Model;

public class Photo {

    private String mTitle;
    private String mPath;

    public Photo(String title, String path) {
        mTitle = title;
        mPath = path;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }
}
