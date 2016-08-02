package com.hashirbaig.developer.phonegalleryapp.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.hashirbaig.developer.phonegalleryapp.Databases.DBCursorWrapper;
import com.hashirbaig.developer.phonegalleryapp.Databases.DatabaseHelper;
import com.hashirbaig.developer.phonegalleryapp.Databases.GalleryDBSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlbumData {

    private static final String TAG = "AlbumData";
    private static AlbumData sAlbumData;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private List<Album> mAlbumList;

    private AlbumData(Context context) {
        mContext = context;
        mAlbumList = new ArrayList<>();
        mDatabase = new DatabaseHelper(context).getWritableDatabase();
        queryDatabase();
    }

    public static AlbumData get(Context context) {
        if(sAlbumData == null) {
            sAlbumData = new AlbumData(context);
        }
        return sAlbumData;
    }

    public Album get(UUID uuid) {
        for (Album album : mAlbumList) {
            if(album.getUUID().equals(uuid))
                return album;
        }
        return null;
    }

    public void queryDatabase() {

        Cursor albumCursor = mDatabase.query(
                GalleryDBSchema.AlbumTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        DBCursorWrapper albumWrapper = new DBCursorWrapper(albumCursor);
        albumWrapper.moveToFirst();
        if(albumWrapper.getCount() > 0) {
            try {
                while (!albumCursor.isAfterLast()) {
                    Album album = albumWrapper.getAlbum();
                    mAlbumList.add(album);

                    Cursor photoCursor = mDatabase.query(
                            GalleryDBSchema.PhotoData.TABLE_NAME,
                            null,
                            GalleryDBSchema.PhotoData.cols.ALBUM_ID + " = ?",
                            new String[]{album.getUUID().toString()},
                            null,
                            null,
                            null
                    );

                    DBCursorWrapper photoWrapper = new DBCursorWrapper(photoCursor);
                    photoWrapper.moveToFirst();
                    if(photoWrapper.getCount() > 0) {
                        try {
                            while (!photoWrapper.isAfterLast()) {
                                album.add(photoWrapper.getPhoto());
                                photoWrapper.moveToNext();
                            }
                        } finally {
                            photoWrapper.close();
                        }
                    }
                    albumWrapper.moveToNext();

                }
            } finally {
                albumWrapper.close();
            }

        }
    }

    public void queryAlbums() {
        File root = Environment.getExternalStorageDirectory();
        searchDirectory(root);
    }

    public List<Album> getAlbumList() {
        return mAlbumList;
    }

    public void addAlbum(Album album) {
        mDatabase.insert(GalleryDBSchema.AlbumTable.TABLE_NAME, null, getAlbumCV(album));
    }

    public void addPhoto(Photo photo) {
        mDatabase.insert(GalleryDBSchema.PhotoData.TABLE_NAME, null, getPhotoCV(photo));
    }

    private ContentValues getPhotoCV(Photo photo) {
        ContentValues values = new ContentValues();
        values.put(GalleryDBSchema.PhotoData.cols.TITLE, photo.getTitle());
        values.put(GalleryDBSchema.PhotoData.cols.PATH, photo.getPath());
        values.put(GalleryDBSchema.PhotoData.cols.ALBUM_ID, photo.getAlbumId().toString());

        return values;
    }

    private ContentValues getAlbumCV(Album album) {
        ContentValues values = new ContentValues();
        values.put(GalleryDBSchema.AlbumTable.cols.TITLE, album.getTitle());
        values.put(GalleryDBSchema.AlbumTable.cols.PATH, album.getPath());
        values.put(GalleryDBSchema.AlbumTable.cols.HIDDEN, album.isHidden());
        values.put(GalleryDBSchema.AlbumTable.cols.UUID, album.getUUID().toString());

        return values;
    }

    private void searchDirectory(File file) {
        UUID uuid = UUID.randomUUID();
        Album album = new Album(file.getPath().substring(file.getPath().lastIndexOf("/") + 1));
        album.setPath(file.getPath());
        album.setUUID(uuid);
        for (File f : file.listFiles()) {
            if(f.isDirectory()) {
                searchDirectory(f);
            } else if(f.toURI().toString().endsWith(".jpg")){
                Photo photo = new Photo(f.getPath().substring(f.getPath().lastIndexOf("/") + 1), f.getPath());
                photo.setAlbumId(uuid);
                album.add(photo);
                addPhoto(photo);
                Log.i(TAG, album.getTitle() + " --- " + photo.getTitle());
            }
        }
        if(album.getPhotos().size() > 0) {
            mAlbumList.add(album);
            addAlbum(album);
        }
    }
}
