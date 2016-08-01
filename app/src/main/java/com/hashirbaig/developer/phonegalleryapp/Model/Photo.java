package com.hashirbaig.developer.phonegalleryapp.Model;

import java.util.UUID;

public class Photo {

    private String mTitle;
    private String mPath;
    private UUID mAlbumId;

    public Photo() {

    }

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

    public UUID getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(UUID albumId) {
        mAlbumId = albumId;
    }
}
