package com.hashirbaig.developer.phonegalleryapp.HostingActivities;

import android.support.v4.app.Fragment;

import com.hashirbaig.developer.phonegalleryapp.MainFragments.AlbumsGridFragment;

public class AlbumsGridActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return AlbumsGridFragment.newInstance();
    }

}
