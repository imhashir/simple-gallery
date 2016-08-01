package com.hashirbaig.developer.phonegalleryapp.Model;

import java.util.ArrayList;
import java.util.List;

public class Album {

    private String mTitle;
    private String mPath;
    private List<Photo> mPhotos = new ArrayList<>();
    private boolean mHidden;

    public Album() {

    }

    public Album(String title) {
        mTitle = title;
        mHidden = mTitle.startsWith(".");
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

    public void add(Photo photo) {
        mPhotos.add(photo);
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }

    public boolean isHidden() {
        return mHidden;
    }

    public void setHidden(boolean hidden) {
        mHidden = hidden;
    }
}
