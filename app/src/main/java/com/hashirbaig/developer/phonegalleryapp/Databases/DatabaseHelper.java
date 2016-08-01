package com.hashirbaig.developer.phonegalleryapp.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, GalleryDBSchema.DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + GalleryDBSchema.AlbumTable.TABLE_NAME + "(" +
                GalleryDBSchema.AlbumTable.cols.TITLE + "," +
                GalleryDBSchema.AlbumTable.cols.LOCATION + ", "  +
                GalleryDBSchema.AlbumTable.cols.HIDDEN + ", " +
                GalleryDBSchema.AlbumTable.cols.ITEMS +
                ")"
        );

        db.execSQL("CREATE TABLE " + GalleryDBSchema.PhotoData.TABLE_NAME + "(" +
                GalleryDBSchema.PhotoData.cols.TITLE + "," +
                GalleryDBSchema.PhotoData.cols.LOCATION +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
