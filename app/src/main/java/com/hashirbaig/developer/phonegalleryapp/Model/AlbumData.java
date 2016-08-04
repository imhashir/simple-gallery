package com.hashirbaig.developer.phonegalleryapp.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.hashirbaig.developer.phonegalleryapp.Databases.DBCursorWrapper;
import com.hashirbaig.developer.phonegalleryapp.Databases.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.hashirbaig.developer.phonegalleryapp.Databases.GalleryDBSchema.*;

public class AlbumData {

    private static final String TAG = "AlbumData";
    private static AlbumData sAlbumData;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private List<Album> mAlbumList;

    private AlbumData(Context context) {
        mContext = context;
        mAlbumList = new ArrayList<>();
        mDatabase = new DatabaseHelper(mContext).getWritableDatabase();
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

        List<Album> newList = new ArrayList<>();
        Cursor albumCursor = mDatabase.query(
                AlbumTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                AlbumTable.cols.DATE + " DESC"
        );

        DBCursorWrapper albumWrapper = new DBCursorWrapper(albumCursor);
        albumWrapper.moveToFirst();
        if(albumWrapper.getCount() > 0) {
            try {
                while (!albumCursor.isAfterLast()) {
                    Album album = albumWrapper.getAlbum();
                    newList.add(album);
                    albumWrapper.moveToNext();

                }
            } finally {
                albumWrapper.close();
            }
        }
        mAlbumList = newList;
    }

    public void queryAlbums() {
        File root = Environment.getExternalStorageDirectory();
        searchDirectory(root);
    }

    public List<Album> getAlbumList() {
        return mAlbumList;
    }

    public void addAlbum(Album album) {
        mDatabase.insert(AlbumTable.TABLE_NAME, null, getAlbumCV(album));
    }

    public void addPhoto(Photo photo) {
        mDatabase.insert(PhotoTable.TABLE_NAME, null, getPhotoCV(photo));
    }

    private ContentValues getPhotoCV(Photo photo) {
        ContentValues values = new ContentValues();
        values.put(PhotoTable.cols.TITLE, photo.getTitle());
        values.put(PhotoTable.cols.PATH, photo.getPath());
        values.put(PhotoTable.cols.ALBUM_ID, photo.getAlbumId().toString());
        values.put(PhotoTable.cols.DATE, photo.getDate().getTime());

        return values;
    }

    private ContentValues getAlbumCV(Album album) {
        ContentValues values = new ContentValues();
        values.put(AlbumTable.cols.TITLE, album.getTitle());
        values.put(AlbumTable.cols.PATH, album.getPath());
        values.put(AlbumTable.cols.HIDDEN, album.isHidden());
        values.put(AlbumTable.cols.UUID, album.getUUID().toString());
        values.put(AlbumTable.cols.DATE, album.getDate().getTime());

        return values;
    }

    private void searchDirectory(File file) {
        UUID uuid = UUID.randomUUID();
        Album album = new Album(file.getPath().substring(file.getPath().lastIndexOf("/") + 1));
        album.setPath(file.getPath());
        album.setUUID(uuid);
        boolean isMedia = false;
        for (File f : file.listFiles()) {
            String fullPath = f.toURI().toString();
            String temp = fullPath.substring(0, fullPath.length() - 1);
            String name = temp.substring(temp.lastIndexOf("/") + 1);
            if(f.isDirectory()) {
                if(name.equals("Android") || has(f.getPath())) {
                    continue;
                } else {
                    searchDirectory(f);
                }
            } else if(fullPath.endsWith(".jpg") || fullPath.endsWith(".png") || fullPath.endsWith(".gif")){
                isMedia = true;
                Log.i(TAG, album.getTitle());
                break;
            }
        }
        if(isMedia) {
            addAlbum(album);
        }
    }

    public boolean has(String a) {
        for(Album album : mAlbumList) {
            String data = album.getPath();
            String newData = a;
            if(data.equals(newData))
                return true;
        }
        return false;
    }
}
