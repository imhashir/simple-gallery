package com.hashirbaig.developer.phonegalleryapp.HostingActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hashirbaig.developer.phonegalleryapp.MainFragments.ImageOnClickFragment;
import com.hashirbaig.developer.phonegalleryapp.Model.Album;
import com.hashirbaig.developer.phonegalleryapp.Model.AlbumData;
import com.hashirbaig.developer.phonegalleryapp.Model.Photo;
import com.hashirbaig.developer.phonegalleryapp.R;

import java.util.List;
import java.util.UUID;

public class ImageOnClickActivity extends AppCompatActivity{

    private ViewPager mViewPager;
    private List<Photo> mPhotoList;
    private Photo mPhoto;
    private Album mAlbum;

    private static final String KEY_UUID = "key_uuid3646";
    private static final String KEY_PATH = "key_path58966";

    public static Intent newIntent(Context context, String path, UUID uuid) {
        Intent i = new Intent(context, ImageOnClickActivity.class);
        i.putExtra(KEY_PATH, path);
        i.putExtra(KEY_UUID, uuid);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_click_image);

        mViewPager = (ViewPager) findViewById(R.id.image_container);
        UUID uuid = (UUID) getIntent().getSerializableExtra(KEY_UUID);
        String path = getIntent().getStringExtra(KEY_PATH);

        mAlbum = AlbumData.get(getApplicationContext()).get(uuid);
        mPhoto = mAlbum.getPhoto(path);
        mPhotoList = mAlbum.getPhotos();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = ImageOnClickFragment.newInstance(mAlbum.getPhotos().get(position).getPath(), mAlbum.getUUID());
                return fragment;
            }

            @Override
            public int getCount() {
                return mPhotoList.size();
            }
        });

        int x = 0;
        for (Photo photo : mPhotoList) {
            if(photo.getPath().equals(mPhoto.getPath())) {
                mViewPager.setCurrentItem(x);
                break;
            }
            x++;
        }

    }
}
