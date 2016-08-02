package com.hashirbaig.developer.phonegalleryapp.HostingActivities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;

import com.hashirbaig.developer.phonegalleryapp.MainFragments.ImagesGridFragment;
import com.hashirbaig.developer.phonegalleryapp.Model.Album;

public class ImagesGridActivity extends SingleFragmentActivity{

    public static final String KEY_ALBUM_UUID = "album_uuid_56999";

    @Override
    public Fragment createFragment() {
        return ImagesGridFragment.newInstance();
    }

    public static Intent newIntent(Context context, Album album) {
        Intent i = new Intent(context, ImagesGridActivity.class);
        i.putExtra(KEY_ALBUM_UUID, album.getUUID());
        return i;
    }
}
