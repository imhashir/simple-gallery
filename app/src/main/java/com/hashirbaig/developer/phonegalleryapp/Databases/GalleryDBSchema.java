package com.hashirbaig.developer.phonegalleryapp.Databases;

import com.hashirbaig.developer.phonegalleryapp.Model.Album;

public class GalleryDBSchema {

    public static final String DATABASE_NAME = "galleryDB.db";
    public static final class AlbumTable {
        public static final String TABLE_NAME = "albums";
        public static class cols {
            public static final String TITLE = "title";
            public static final String LOCATION = "location";
            public static final String HIDDEN = "hidden";
            public static final String UUID = "uuid";

        }
    }

    public static final class PhotoData {
        public static final String TABLE_NAME = "photos";
        public static class cols {
            public static final String TITLE = "title";
            public static final String LOCATION = "location";
            public static final String ALBUM_ID = "albumId";

        }
    }

}
