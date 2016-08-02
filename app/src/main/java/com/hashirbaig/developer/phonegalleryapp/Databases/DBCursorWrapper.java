package com.hashirbaig.developer.phonegalleryapp.Databases;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.hashirbaig.developer.phonegalleryapp.Model.Album;
import com.hashirbaig.developer.phonegalleryapp.Model.Photo;

import java.util.UUID;

import static com.hashirbaig.developer.phonegalleryapp.Databases.GalleryDBSchema.*;

public class DBCursorWrapper extends CursorWrapper{

    public DBCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Album getAlbum() {
        Album album = new Album();

        String titleString = getString(getColumnIndex(AlbumTable.cols.TITLE));
        String pathString = getString(getColumnIndex(AlbumTable.cols.PATH));
        boolean hiddenBool = getString(getColumnIndex(AlbumTable.cols.HIDDEN)) == "1";
        String uuidString = getString(getColumnIndex(AlbumTable.cols.UUID));

        album.setTitle(titleString);
        album.setPath(pathString);
        album.setHidden(hiddenBool);
        album.setUUID(UUID.fromString(uuidString));
        return album;
    }

    public Photo getPhoto() {
        Photo photo = new Photo();

        String titleString = getString(getColumnIndex(PhotoData.cols.TITLE));
        String pathString = getString(getColumnIndex(PhotoData.cols.PATH));
        String uuidString = getString(getColumnIndex(PhotoData.cols.ALBUM_ID));

        photo.setTitle(titleString);
        photo.setPath(pathString);
        photo.setAlbumId(UUID.fromString(uuidString));
        return photo;
    }

}
