package com.hashirbaig.developer.phonegalleryapp.Threads;

import android.os.HandlerThread;

public class ThumbnailLoader<T> extends HandlerThread{

    private static final String TAG = "ThumbnailLoader";
    public ThumbnailLoader() {
        super(TAG);
    }

    public void queueThumbnail(T target, String path) {

    }
}
