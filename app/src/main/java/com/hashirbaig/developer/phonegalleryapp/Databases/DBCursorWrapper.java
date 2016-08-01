package com.hashirbaig.developer.phonegalleryapp.Databases;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.hashirbaig.developer.phonegalleryapp.Model.Album;

import static com.hashirbaig.developer.phonegalleryapp.Databases.GalleryDBSchema.*;

public class DBCursorWrapper extends CursorWrapper{

    public DBCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Album getAlbum() {
        Album album = new Album();

        String titleString = getString(getColumnIndex(AlbumTable.cols.TITLE));
        String pathString = getString(getColumnIndex(AlbumTable.cols.LOCATION));
        boolean hiddenBool = getString(getColumnIndex(AlbumTable.cols.HIDDEN)) == "1";
        int itemsCount = getInt(getColumnIndex(AlbumTable.cols.ITEMS));

        album.setTitle(titleString);
        album.setPath(pathString);
        album.setHidden(hiddenBool);
        return album;
    }

}
