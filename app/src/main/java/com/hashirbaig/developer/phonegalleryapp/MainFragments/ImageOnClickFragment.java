package com.hashirbaig.developer.phonegalleryapp.MainFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.module.GlideModule;
import com.hashirbaig.developer.phonegalleryapp.Model.AlbumData;
import com.hashirbaig.developer.phonegalleryapp.Model.Photo;
import com.hashirbaig.developer.phonegalleryapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class ImageOnClickFragment extends Fragment{

    private static final String KEY_IMAGE_PATH = "image_path555";
    private static final String KEY_UUID_ALBUM = "uuid_key_36313";

    private ImageViewTouch mImageView;
    private Photo mPhoto;

    public static ImageOnClickFragment newInstance(String path, UUID uuid) {
        Bundle args = new Bundle();
        args.putString(KEY_IMAGE_PATH, path);
        args.putSerializable(KEY_UUID_ALBUM, uuid);
        ImageOnClickFragment fragment = new ImageOnClickFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID uuid = (UUID) getArguments().getSerializable(KEY_UUID_ALBUM);
        String path = getArguments().getString(KEY_IMAGE_PATH);

        mPhoto = AlbumData.get(getActivity()).get(uuid).getPhoto(path);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.image_on_click_fragment,  container, false);
        mImageView = (ImageViewTouch) v.findViewById(R.id.image_opened_view);

        Glide.with(getActivity())
                .load(mPhoto.getPath())
                .into(mImageView);

        mImageView.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                if(activity.getSupportActionBar().isShowing())
                    activity.getSupportActionBar().hide();
                else
                    activity.getSupportActionBar().show();
            }
        });
        return v;
    }
}
