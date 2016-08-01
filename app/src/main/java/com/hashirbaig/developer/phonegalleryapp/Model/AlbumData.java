package com.hashirbaig.developer.phonegalleryapp.Model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Picture;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.hashirbaig.developer.phonegalleryapp.Databases.GalleryDBSchema;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class AlbumData {

    private static final String TAG = "AlbumData";
    private static AlbumData sAlbumData;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private List<Album> mAlbumList;

    private AlbumData(Context context) {
        mContext = context;
        mAlbumList = new ArrayList<>();
    }

    public static AlbumData get(Context context) {
        if(sAlbumData == null) {
            sAlbumData = new AlbumData(context);
        }
        return sAlbumData;
    }

    public void queryAlbums() {
        File root = Environment.getExternalStorageDirectory();
        searchDirectory(root);
    }

    public List<Album> getAlbumList() {
        return mAlbumList;
    }

    private ContentValues getPhotoCV(Photo photo) {
        ContentValues values = new ContentValues();
        values.put(GalleryDBSchema.PhotoData.cols.TITLE, photo.getTitle());
        values.put(GalleryDBSchema.PhotoData.cols.LOCATION, photo.getPath());

        return values;
    }

    private ContentValues getAlbumCV(Album album) {
        ContentValues values = new ContentValues();
        values.put(GalleryDBSchema.AlbumTable.cols.TITLE, album.getTitle());
        values.put(GalleryDBSchema.AlbumTable.cols.LOCATION, album.getPath());
        values.put(GalleryDBSchema.AlbumTable.cols.HIDDEN, album.isHidden());
        values.put(GalleryDBSchema.AlbumTable.cols.ITEMS, album.getPhotos().size());

        return values;
    }

    private void searchDirectory(File file) {
        Album album = new Album(file.getPath().substring(file.getPath().lastIndexOf("/") + 1));
        album.setPath(file.getPath());
        for (File f : file.listFiles()) {
            if(f.isDirectory()) {
                searchDirectory(f);
            } else if(f.toURI().toString().endsWith(".jpg")){
                Photo photo = new Photo(f.getPath().substring(f.getPath().lastIndexOf("/") + 1), f.getPath());
                album.add(photo);
                Log.i(TAG, album.getTitle() + " --- " + photo.getTitle());
            }
        }
        if(album.getPhotos().size() > 0) {
            mAlbumList.add(album);
        }
    }

}
