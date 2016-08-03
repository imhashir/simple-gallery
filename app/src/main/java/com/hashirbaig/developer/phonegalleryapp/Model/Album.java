package com.hashirbaig.developer.phonegalleryapp.Model;

import android.content.pm.PackageManager;
import android.graphics.Path;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Album {

    private String mTitle;
    private String mPath;
    private List<Photo> mPhotos = new ArrayList<>();
    private boolean mHidden;
    private UUID mUUID;
    private Date mDate;

    public Album() {
        mUUID = UUID.randomUUID();
    }

    public Album(String title) {
        mTitle = title;
        mHidden = mTitle.startsWith(".");
        mUUID = UUID.randomUUID();
    }

    public Photo getPhoto(String path) {
        for(Photo photo : mPhotos) {
            if(photo.getPath().equals(path))
                return photo;
        }
        return null;
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
        mDate = new Date(new File(path).lastModified() * 1000);
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

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
