package com.hashirbaig.developer.phonegalleryapp.Model;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class Photo {

    private String mTitle;
    private String mPath;
    private UUID mAlbumId;
    private Date mDate;

    public Photo() {
        mDate = new Date();
    }

    public Photo(String path) {
        mDate = new Date(new File(path).lastModified() * 1000);
        mTitle = path.substring(path.lastIndexOf("/") + 1);
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
        mDate = new Date(new File(path).lastModified() * 1000);
    }

    public UUID getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(UUID albumId) {
        mAlbumId = albumId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
