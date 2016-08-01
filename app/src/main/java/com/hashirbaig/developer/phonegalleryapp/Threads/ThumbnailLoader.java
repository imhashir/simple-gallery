package com.hashirbaig.developer.phonegalleryapp.Threads;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.util.LruCache;

import com.hashirbaig.developer.phonegalleryapp.Helper.PictureUtils;

import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ThumbnailLoader<T> extends HandlerThread{

    private static final String TAG = "ThumbnailLoader";
    private static int MESSAGE_DOWNLOAD = 1001;

    private LruCache<String, Bitmap> mCache = new LruCache<>(10 * 1024);
    private Handler mRequestHandler;
    private Handler mResponseHandler;
    private ConcurrentMap<T, String> mRequestMap = new ConcurrentHashMap<>();
    private ThumbnailLoaderListener<T> mThumbnailLoaderListener;

    public ThumbnailLoader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    public interface ThumbnailLoaderListener<T> {
        void onThumbnailDownloaded(T target, Bitmap bitmap);
    }

    public void setThumbnailLoadedListener(ThumbnailLoaderListener<T> target) {
        mThumbnailLoaderListener = target;
    }

    public void queueThumbnail(T target, String path) {

        if(path == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, path);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
                    .sendToTarget();
        }
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();

        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == MESSAGE_DOWNLOAD) {
                    final T target = (T) msg.obj;

                    final String path = mRequestMap.get(target);
                    final Bitmap bitmap;
                    if (mCache.get(path) == null){
                        bitmap = PictureUtils.getScaledBitmap(path, 90, 90);
                        mCache.put(path, bitmap);
                    } else {
                        bitmap = mCache.get(path);
                    }

                    mResponseHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if(mRequestMap.get(target) != path)
                                return;

                            mRequestMap.remove(target);
                            mThumbnailLoaderListener.onThumbnailDownloaded(target, bitmap);
                        }
                    });
                }
            }
        };
    }
}
