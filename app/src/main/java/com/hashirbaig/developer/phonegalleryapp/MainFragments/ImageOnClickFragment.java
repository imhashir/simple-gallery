package com.hashirbaig.developer.phonegalleryapp.MainFragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hashirbaig.developer.phonegalleryapp.Model.AlbumData;
import com.hashirbaig.developer.phonegalleryapp.Model.Photo;
import com.hashirbaig.developer.phonegalleryapp.R;

import java.util.UUID;

public class ImageOnClickFragment extends Fragment{

    private static final String KEY_IMAGE_PATH = "image_path555";
    private static final String KEY_UUID_ALBUM = "uuid_key_36313";

    private ImageView mImageView;
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
        mImageView = (ImageView) v.findViewById(R.id.image_opened_view);
        Bitmap bitmap = BitmapFactory.decodeFile(mPhoto.getPath());
        mImageView.setImageBitmap(bitmap);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
