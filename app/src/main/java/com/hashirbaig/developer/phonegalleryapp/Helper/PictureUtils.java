package com.hashirbaig.developer.phonegalleryapp.Helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PictureUtils {

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int inSampleSize = 1;
        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        if(srcWidth > destWidth || srcHeight > destHeight) {
            if(srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(path, options);
    }

}
