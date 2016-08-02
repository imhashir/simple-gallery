package com.hashirbaig.developer.phonegalleryapp.MainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hashirbaig.developer.phonegalleryapp.HostingActivities.ImageOnClickActivity;
import com.hashirbaig.developer.phonegalleryapp.HostingActivities.ImagesGridActivity;
import com.hashirbaig.developer.phonegalleryapp.Model.Album;
import com.hashirbaig.developer.phonegalleryapp.Model.AlbumData;
import com.hashirbaig.developer.phonegalleryapp.Model.Photo;
import com.hashirbaig.developer.phonegalleryapp.R;

import java.util.List;
import java.util.UUID;

public class ImagesGridFragment extends Fragment{

    private Album mAlbum;
    private RecyclerView mGridView;
    private ImageAdapter mAdapter;

    public static ImagesGridFragment newInstance() {
        return new ImagesGridFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID uuid = (UUID) getActivity().getIntent().getSerializableExtra(ImagesGridActivity.KEY_ALBUM_UUID);
        mAlbum = AlbumData.get(getActivity()).get(uuid);

        getActivity().setTitle(mAlbum.getTitle());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.gallery_grid_layout, container, false);

        mGridView = (RecyclerView) v.findViewById(R.id.grid_view);
        mGridView.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.no_of_cols)));
        updateUI();
        return v;
    }

    public void updateUI(){
        if(mAdapter == null) {
            mAdapter = new ImageAdapter(mAlbum.getPhotos());
            mGridView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ImageHolder extends RecyclerView.ViewHolder
                                        implements View.OnClickListener{

        private ImageView mImageView;
        private Photo photo;

        public ImageHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.image_thumb_container);
            v.setOnClickListener(this);
        }

        private void bindThumbnail(Photo p) {
            photo = p;
            Glide.with(getActivity())
                    .load(p.getPath())
                    .into(mImageView);
        }

        @Override
        public void onClick(View v) {
            Intent i = ImageOnClickActivity.newIntent(getActivity(), photo.getPath(), photo.getAlbumId());
            startActivity(i);
        }
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {

        private List<Photo> mPhotosList;

        public ImageAdapter(List<Photo> list) {
            mPhotosList = list;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(getActivity()).inflate(R.layout.single_image_layout, parent, false);
            return new ImageHolder(v);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            holder.bindThumbnail(mPhotosList.get(position));
        }

        @Override
        public int getItemCount() {
            return mPhotosList.size();
        }
    }
}
