package com.hashirbaig.developer.phonegalleryapp.HostingActivities;

import android.support.v4.app.Fragment;

import com.hashirbaig.developer.phonegalleryapp.MainFragments.GalleryGridFragment;

public class GalleyGridActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return GalleryGridFragment.newInstance();
    }

}
